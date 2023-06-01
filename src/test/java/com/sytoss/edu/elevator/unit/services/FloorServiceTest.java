package com.sytoss.edu.elevator.unit.services;

import com.sytoss.edu.elevator.bom.house.House;
import com.sytoss.edu.elevator.bom.house.floors.FirstFloor;
import com.sytoss.edu.elevator.bom.house.floors.Floor;
import com.sytoss.edu.elevator.bom.house.floors.MiddleFloor;
import com.sytoss.edu.elevator.services.FloorService;
import com.sytoss.edu.elevator.services.HouseService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class FloorServiceTest {

    private final House house = spy(House.class);

    private final HouseService houseService = mock(HouseService.class);

    private final FloorService floorService = new FloorService(houseService);

    @Test
    public void goUpCabinRequestToFirstFloorTest() {
        house.setId(123L);
        FirstFloor firstFloor = mock(FirstFloor.class);

        when(house.getFloors()).thenReturn(List.of(firstFloor));
        when(houseService.getHouse(0L)).thenReturn(house);

        floorService.goUpCabinRequest(0L, 1);
        verify(firstFloor).pressUpButton();
    }

    @Test
    public void goUpCabinRequestToMiddleFloorTest() {
        house.setId(123L);
        FirstFloor firstFloor = mock(FirstFloor.class);
        MiddleFloor middleFloor = mock(MiddleFloor.class);

        List<Floor> list = new ArrayList<>();
        list.add(firstFloor);
        for (int i = 2; i <= 4; ++i) {
            list.add(middleFloor);
        }
        when(house.getFloors()).thenReturn(list);
        when(houseService.getHouse(0L)).thenReturn(house);
        floorService.goUpCabinRequest(0L, 2);
        verify(middleFloor).pressUpButton();
    }
}
