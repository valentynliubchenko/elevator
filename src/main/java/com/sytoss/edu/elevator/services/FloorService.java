package com.sytoss.edu.elevator.services;

import com.sytoss.edu.elevator.bom.house.House;
import com.sytoss.edu.elevator.bom.house.buttons.FloorWithUpButton;
import com.sytoss.edu.elevator.bom.house.floors.Floor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class FloorService {

    private final HouseService houseService;

    public void goUpCabinRequest(long houseId, int floorNumber) {
        House house = houseService.getHouse(houseId);

        Floor floor = house.getFloors().get(floorNumber - 1);

        if (floor instanceof FloorWithUpButton) {
            ((FloorWithUpButton) floor).pressUpButton();
        }
    }
}