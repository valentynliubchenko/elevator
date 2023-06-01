package com.sytoss.edu.elevator.unit.commands;

import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.house.House;
import com.sytoss.edu.elevator.bom.house.floors.Floor;
import com.sytoss.edu.elevator.commands.VisitFloorCommand;
import com.sytoss.edu.elevator.services.HouseService;
import com.sytoss.edu.elevator.services.ShaftService;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static com.sytoss.edu.elevator.commands.CommandManager.SHAFT_PARAM;
import static org.mockito.Mockito.*;

public class VisitFloorCommandTest {

    private final House house = mock(House.class);

    private final HouseService houseService = mock(HouseService.class);

    private final ShaftService shaftService = mock(ShaftService.class);

    private final VisitFloorCommand visitFloorCommand = new VisitFloorCommand(houseService, shaftService);

    @Test
    public void executeTest() {
        long shaftId = 7L;
        int cabinPosition = 2;

        when(houseService.getHouseByShaftId(shaftId)).thenReturn(house);

        Shaft shaft = mock(Shaft.class);
        when(shaft.getId()).thenReturn(shaftId);
        when(shaft.getCabinPosition()).thenReturn(cabinPosition);

        Floor floor = mock(Floor.class);
        when(floor.getFloorNumber()).thenReturn(cabinPosition + 1);
        when(house.nextFloor(shaft.getCabinPosition())).thenReturn(floor);

        HashMap<String, Object> params = new HashMap<>();
        params.put(SHAFT_PARAM, shaft);

        visitFloorCommand.execute(params);

        verify(houseService).getHouseByShaftId(shaft.getId());
        verify(house).nextFloor(shaft.getCabinPosition());
        verify(floor).getFloorNumber();
        verify(shaftService).updateSequenceOfStopsFromDb(shaft);
        verify(shaft).setCabinPosition(floor.getFloorNumber());
    }
}