package com.sytoss.edu.elevator.converters;

import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.house.House;
import com.sytoss.edu.elevator.bom.house.HouseBuilder;
import com.sytoss.edu.elevator.dto.HouseDTO;
import com.sytoss.edu.elevator.dto.ShaftDTO;
import com.sytoss.edu.elevator.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HouseConverter {

    private final HouseBuilder houseBuilder;

    private final ShaftConverter shaftConverter;

    public HouseDTO toDTO(House house) {
        var orderSequenceOfStops = house.getElevatorDriver().getOrderSequenceOfStops();
        return HouseDTO.builder()
                .numberOfFloors(house.getFloors().size())
                .numberOfShafts(house.getShafts().size())
                .orderSequenceOfStops(JsonUtil.orderSequenceToStringInJSON(orderSequenceOfStops))
                .build();
    }

    public House fromDTO(HouseDTO houseDTO, List<ShaftDTO> shaftDTOList) {
        House house = houseBuilder.build(houseDTO.getNumberOfShafts(), houseDTO.getNumberOfFloors());
        house.setId(houseDTO.getId());
        house.getShafts().clear();
        for (ShaftDTO shaftDTO : shaftDTOList) {
            Shaft shaft = shaftConverter.fromDTO(shaftDTO);
            house.getShafts().add(shaft);
        }
        return house;
    }
}
