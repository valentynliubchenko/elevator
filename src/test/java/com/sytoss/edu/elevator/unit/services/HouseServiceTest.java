package com.sytoss.edu.elevator.unit.services;

import com.sytoss.edu.elevator.bom.ElevatorDriver;
import com.sytoss.edu.elevator.bom.SequenceOfStops;
import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.house.House;
import com.sytoss.edu.elevator.bom.house.HouseBuilder;
import com.sytoss.edu.elevator.commands.CommandManager;
import com.sytoss.edu.elevator.converters.HouseConverter;
import com.sytoss.edu.elevator.converters.ShaftConverter;
import com.sytoss.edu.elevator.dto.HouseDTO;
import com.sytoss.edu.elevator.dto.ShaftDTO;
import com.sytoss.edu.elevator.events.OrderSequenceOfStopsChangedEvent;
import com.sytoss.edu.elevator.params.HouseParams;
import com.sytoss.edu.elevator.repositories.HouseRepository;
import com.sytoss.edu.elevator.repositories.ShaftRepository;
import com.sytoss.edu.elevator.services.CabinService;
import com.sytoss.edu.elevator.services.EngineService;
import com.sytoss.edu.elevator.services.HouseService;
import com.sytoss.edu.elevator.services.ShaftService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class HouseServiceTest {

    private final HouseRepository houseRepository = mock(HouseRepository.class);

    private final ShaftRepository shaftRepository = mock(ShaftRepository.class);

    private final CommandManager commandManager = mock(CommandManager.class);

    private final HouseBuilder houseBuilder = new HouseBuilder(mock(CommandManager.class));

    private final ShaftConverter shaftConverter = new ShaftConverter();

    private final HouseConverter houseConverter = spy(new HouseConverter(houseBuilder, shaftConverter));

    private final EngineService engineService = mock(EngineService.class);

    private final CabinService cabinService = mock(CabinService.class);

    private final ShaftService shaftService = mock(ShaftService.class);

    private final HouseService houseService = new HouseService(houseRepository, shaftRepository, houseConverter, commandManager, engineService, cabinService, shaftService);

    @Test
    public void saveRequestTest() {
        HouseDTO insertedHouseDTO = mock(HouseDTO.class);

        HouseParams houseParams = spy(HouseParams.builder().build());
        houseParams.setNumberOfShafts(2);

        when(houseRepository.save(any())).thenReturn(insertedHouseDTO);
        when(insertedHouseDTO.getId()).thenReturn(7L);

        houseService.saveRequest(houseParams);

        verify(houseRepository).save(any());
        verify(shaftRepository).saveAll(any());
    }

    @Test
    public void getHouseTest() {
        int numberOfShafts = 3;
        int numberOfFloors = 5;
        long houseId = 77;

        List<ShaftDTO> shaftDTOList = new ArrayList<>();
        for (int i = 0; i < numberOfShafts; i++) {
            shaftDTOList.add(mock(ShaftDTO.class));
        }

        HouseDTO houseDTO = mock(HouseDTO.class);
        when(houseDTO.getId()).thenReturn(houseId);
        when(houseDTO.getShafts()).thenReturn(shaftDTOList);
        when(houseDTO.getOrderSequenceOfStops()).thenReturn(null);
        when(houseDTO.getNumberOfShafts()).thenReturn(numberOfShafts);
        when(houseDTO.getNumberOfFloors()).thenReturn(numberOfFloors);

        when(houseRepository.getReferenceById(houseId)).thenReturn(houseDTO);
        when(shaftRepository.findByHouseDTOId(houseDTO.getId())).thenReturn(shaftDTOList);

        House house = houseService.getHouse(houseId);

        verify(houseRepository).getReferenceById(houseId);
        verify(shaftRepository).findByHouseDTOId(houseId);
        verify(houseConverter).fromDTO(any(), any());

        Assertions.assertTrue(house.getElevatorDriver().getOrderSequenceOfStops().isEmpty());
        Assertions.assertEquals(numberOfFloors, house.getFloors().size());
        Assertions.assertEquals(numberOfShafts, house.getShafts().size());
        house.getShafts().forEach(shaft -> {
            Assertions.assertEquals(2, shaft.getShaftListeners().size());
            Assertions.assertEquals(1, shaft.getEngine().getEngineListeners().size());
            Assertions.assertEquals(2, shaft.getCabin().getCabinListeners().size());
        });
    }

    @Test
    public void getHouseCabinIsMovingTest() {
        int numberOfShafts = 3;
        int numberOfFloors = 5;
        long houseId = 77;

        List<ShaftDTO> shaftDTOList = new ArrayList<>();
        for (int i = 0; i < numberOfShafts; i++) {
            shaftDTOList.add(spy(ShaftDTO.class));
            shaftDTOList.get(i).setSequenceOfStops("{\"id\":123,\"stopFloors\":[1,2,3],\"direction\":\"UPWARDS\"}");
        }

        HouseDTO houseDTO = mock(HouseDTO.class);
        when(houseDTO.getId()).thenReturn(houseId);
        when(houseDTO.getShafts()).thenReturn(shaftDTOList);
        when(houseDTO.getOrderSequenceOfStops()).thenReturn(null);
        when(houseDTO.getNumberOfShafts()).thenReturn(numberOfShafts);
        when(houseDTO.getNumberOfFloors()).thenReturn(numberOfFloors);

        when(houseRepository.getReferenceById(houseId)).thenReturn(houseDTO);
        when(shaftRepository.findByHouseDTOId(houseDTO.getId())).thenReturn(shaftDTOList);

        House house = houseService.getHouse(houseId);

        verify(houseRepository).getReferenceById(houseId);
        verify(shaftRepository).findByHouseDTOId(houseId);
        verify(houseConverter).fromDTO(any(), any());

        Assertions.assertTrue(house.getElevatorDriver().getOrderSequenceOfStops().isEmpty());
        Assertions.assertEquals(numberOfFloors, house.getFloors().size());
        Assertions.assertEquals(numberOfShafts, house.getShafts().size());
        house.getShafts().forEach(shaft -> {
            Assertions.assertEquals(2, shaft.getShaftListeners().size());
            Assertions.assertEquals(1, shaft.getEngine().getEngineListeners().size());
            Assertions.assertEquals(2, shaft.getCabin().getCabinListeners().size());
            Assertions.assertEquals(1, shaft.getSequenceOfStops().getSequenceOfStopsListeners().size());
        });
    }

    @Test
    public void getHouseByShaftId() {
        long houseId = 55;
        long shaftId = 77;
        ShaftDTO shaftDTO = mock(ShaftDTO.class);
        HouseDTO houseDTO = mock(HouseDTO.class);

        when(shaftRepository.getReferenceById(shaftId)).thenReturn(shaftDTO);
        when(shaftDTO.getHouseDTO()).thenReturn(houseDTO);
        when(houseDTO.getId()).thenReturn(houseId);

        houseService.getHouseByShaftId(shaftId);

        verify(shaftRepository).getReferenceById(shaftId);
        verify(shaftDTO).getHouseDTO();
        verify(houseDTO).getId();

        verify(houseRepository).getReferenceById(houseId);
    }

    @Test
    public void handleOrderSequenceOfStopsChangedTest() {
        Long houseId = 2L;
        Direction direction = Direction.UPWARDS;
        Long sequenceId = 3L;

        House house = mock(House.class);
        when(house.getId()).thenReturn(houseId);

        ElevatorDriver elevatorDriver = mock(ElevatorDriver.class);
        SequenceOfStops sequence = new SequenceOfStops();
        sequence.setId(sequenceId);
        sequence.setDirection(direction);
        sequence.setStopFloors(List.of(1, 2, 3));
        when(elevatorDriver.getOrderSequenceOfStops()).thenReturn(List.of(sequence));
        when(house.getElevatorDriver()).thenReturn(elevatorDriver);

        OrderSequenceOfStopsChangedEvent event = new OrderSequenceOfStopsChangedEvent(house);

        String orderSequenceString = "[{\"id\":" + sequenceId + ",\"stopFloors\":[1,2,3],\"direction\":\"" + direction + "\"}]";

        houseService.handleOrderSequenceOfStopsChanged(event);

        verify(houseRepository).updateOrderById(houseId, orderSequenceString);
    }
}
