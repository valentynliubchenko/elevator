package com.sytoss.edu.elevator.services;

import com.sytoss.edu.elevator.bom.ElevatorDriver;
import com.sytoss.edu.elevator.bom.enums.DoorState;
import com.sytoss.edu.elevator.bom.enums.EngineState;
import com.sytoss.edu.elevator.bom.enums.OverWeightState;
import com.sytoss.edu.elevator.bom.house.House;
import com.sytoss.edu.elevator.commands.CommandManager;
import com.sytoss.edu.elevator.converters.HouseConverter;
import com.sytoss.edu.elevator.dto.HouseDTO;
import com.sytoss.edu.elevator.dto.ShaftDTO;
import com.sytoss.edu.elevator.events.OrderSequenceOfStopsChangedEvent;
import com.sytoss.edu.elevator.params.HouseParams;
import com.sytoss.edu.elevator.repositories.HouseRepository;
import com.sytoss.edu.elevator.repositories.ShaftRepository;
import com.sytoss.edu.elevator.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class HouseService implements OrderSequenceOfStopsListener {

    private final HouseRepository houseRepository;

    private final ShaftRepository shaftRepository;

    private final HouseConverter houseConverter;

    private final CommandManager commandManager;

    private final EngineService engineService;

    private final CabinService cabinService;

    private final ShaftService shaftService;

    public long saveRequest(HouseParams houseParams) {
        HouseDTO houseDTO = HouseDTO.builder()
                .numberOfFloors(houseParams.getNumberOfFloors())
                .numberOfShafts(houseParams.getNumberOfShafts())
                .build();

        houseDTO.setShafts(IntStream.range(0, houseParams.getNumberOfShafts()).mapToObj(el -> ShaftDTO.builder()
                .houseDTO(houseDTO)
                .doorState(DoorState.CLOSED)
                .engineState(EngineState.STAYING)
                .overweightState(OverWeightState.NOT_OVERWEIGHT)
                .cabinPosition(1)
                .build()).toList());

        HouseDTO insertedHouseDTO = houseRepository.save(houseDTO);
        shaftRepository.saveAll(houseDTO.getShafts());
        return insertedHouseDTO.getId();
    }

    @Transactional
    public House getHouse(long houseId) {
        try {
            HouseDTO houseDTO = houseRepository.getReferenceById(houseId);
            List<ShaftDTO> shaftDTOList = shaftRepository.findByHouseDTOId(houseDTO.getId());

            House house = houseConverter.fromDTO(houseDTO, shaftDTOList);

            ElevatorDriver elevatorDriver = new ElevatorDriver(commandManager);
            elevatorDriver.setOrderSequenceOfStops(JsonUtil.stringJSONToOrderSequence(houseDTO.getOrderSequenceOfStops()));
            elevatorDriver.addListener(this);

            house.setElevatorDriver(elevatorDriver);
            house.getShafts().forEach(shaft -> {
                if (shaft.isCabinMoving()) {
                    shaft.getSequenceOfStops().addListener(shaftService);
                }

                shaft.getEngine().addListener(engineService);

                shaft.getCabin().addListener(cabinService);
                shaft.getCabin().addListener(elevatorDriver);

                shaft.addShaftListener(shaftService);
                shaft.addShaftListener(elevatorDriver);
            });

            return house;
        } catch (Exception e) {
            log.error("Unexpected error occurs, {}", e.getMessage());
            return null;
        }
    }

    @Transactional
    public House getHouseByShaftId(long shaftId) {
        long houseId = shaftRepository.getReferenceById(shaftId).getHouseDTO().getId();
        return getHouse(houseId);
    }

    @Override
    public void handleOrderSequenceOfStopsChanged(OrderSequenceOfStopsChangedEvent event) {
        House house = event.getHouse();
        String orderSequenceOfStops = JsonUtil.orderSequenceToStringInJSON(house.getElevatorDriver().getOrderSequenceOfStops());
        houseRepository.updateOrderById(house.getId(), orderSequenceOfStops);
    }
}