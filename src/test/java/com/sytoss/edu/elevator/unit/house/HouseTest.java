package com.sytoss.edu.elevator.unit.house;

import com.sytoss.edu.elevator.bom.ElevatorDriver;
import com.sytoss.edu.elevator.bom.SequenceOfStops;
import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.house.House;
import com.sytoss.edu.elevator.bom.house.HouseBuilder;
import com.sytoss.edu.elevator.commands.CommandManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class HouseTest {

    private final HouseBuilder houseBuilder = new HouseBuilder(mock(CommandManager.class));

    private final House house = spy(houseBuilder.build(2, 16));

    private final ElevatorDriver elevatorDriver = mock(ElevatorDriver.class);

    @BeforeEach
    public void elevatorDriverSetting() {
        house.setElevatorDriver(elevatorDriver);
    }

    @Test
    public void findNearestCabinTest() {
        List<SequenceOfStops> orderSequenceOfStops = new ArrayList<>();
        SequenceOfStops sequence1 = new SequenceOfStops();
        sequence1.setId(123L);
        sequence1.setDirection(Direction.UPWARDS);
        sequence1.setStopFloors(List.of(3));
        orderSequenceOfStops.add(sequence1);

        when(elevatorDriver.getOrderSequenceOfStops()).thenReturn(orderSequenceOfStops);

        house.getShafts().get(0).setCabinPosition(12);
        house.getShafts().get(0).setId(0L);
        house.getShafts().get(1).setId(1L);

        Shaft shaft = house.findNearestCabin();

        Assertions.assertEquals(1L, shaft.getId());
    }

    @Test
    public void addNewSequenceToOrderTest() {
        int floorNumber = 5;
        Direction direction = Direction.UPWARDS;

        house.addNewSequenceToOrder(floorNumber, direction);

        verify(elevatorDriver).addNewSequenceToOrder(floorNumber, direction);
        verify(elevatorDriver).getOrderSequenceOfStopsListeners();
    }

    @Test
    public void removeSequenceFromOrderTest() {
        house.removeSequenceFromOrder();

        verify(elevatorDriver).removeSequenceFromOrder();
        verify(elevatorDriver).getOrderSequenceOfStopsListeners();
    }

    @Test
    public void updateSequenceTest() {
        Shaft shaft = mock(Shaft.class);
        when(elevatorDriver.getOrderSequenceOfStops()).thenReturn(List.of(new SequenceOfStops()));

        house.updateSequence(shaft);

        verify(shaft).updateSequence(any());
        verify(house).removeSequenceFromOrder();
    }
}