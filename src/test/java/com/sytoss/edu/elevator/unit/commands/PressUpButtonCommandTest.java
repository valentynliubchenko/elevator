package com.sytoss.edu.elevator.unit.commands;

import com.sytoss.edu.elevator.bom.ElevatorDriver;
import com.sytoss.edu.elevator.bom.SequenceOfStops;
import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.house.House;
import com.sytoss.edu.elevator.commands.Command;
import com.sytoss.edu.elevator.commands.CommandManager;
import com.sytoss.edu.elevator.commands.FindNearestCabinCommand;
import com.sytoss.edu.elevator.commands.PressUpButtonCommand;
import com.sytoss.edu.elevator.services.ShaftService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sytoss.edu.elevator.commands.CommandManager.*;
import static org.mockito.Mockito.*;


public class PressUpButtonCommandTest {

    private final ElevatorDriver elevatorDriver = mock(ElevatorDriver.class);

    private final CommandManager commandManager = mock(CommandManager.class);

    private final ShaftService shaftService = mock(ShaftService.class);

    private final House house = mock(House.class);

    private final PressUpButtonCommand pressUpButtonCommand = new PressUpButtonCommand(commandManager, shaftService);

    @Test
    public void executeTest() {
        HashMap<String, Object> params = new HashMap<>();

        params.put(FLOOR_NUMBER_PARAM, 5);
        params.put(DIRECTION_PARAM, Direction.UPWARDS);
        params.put(HOUSE_PARAM, house);

        when(commandManager.getCommand(Command.FIND_NEAREST_CABIN_COMMAND)).thenReturn(mock(FindNearestCabinCommand.class));
        when(house.getElevatorDriver()).thenReturn(elevatorDriver);
        when(elevatorDriver.getOrderSequenceOfStops()).thenReturn(new ArrayList<>(List.of(new SequenceOfStops())));

        pressUpButtonCommand.execute(params);

        verify(house).addNewSequenceToOrder(5, Direction.UPWARDS);
        verify(commandManager.getCommand(Command.FIND_NEAREST_CABIN_COMMAND)).execute(argThat((arg) -> (arg.get(HOUSE_PARAM) == house)
                && (arg.get(DIRECTION_PARAM) == Direction.UPWARDS)
                && ((int) arg.get(FLOOR_NUMBER_PARAM) == 5)));
    }
}
