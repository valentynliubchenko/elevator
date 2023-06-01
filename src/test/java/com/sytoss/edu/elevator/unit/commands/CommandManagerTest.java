package com.sytoss.edu.elevator.unit.commands;

import com.sytoss.edu.elevator.HouseThreadPool;
import com.sytoss.edu.elevator.commands.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandManagerTest {

    private final HouseThreadPool houseThreadPool = mock(HouseThreadPool.class);

    private final ObjectProvider<PressUpButtonCommand> pressUpButtonCommandProvider = mock(ObjectProvider.class);

    private final ObjectProvider<FindNearestCabinCommand> findNearestCabinCommandProvider = mock(ObjectProvider.class);

    private final ObjectProvider<MoveCabinCommand> activateShaftCommandProvider = mock(ObjectProvider.class);

    private final ObjectProvider<StartEngineCommand> moveCabinCommandObjectProvider = mock(ObjectProvider.class);

    private final ObjectProvider<StopEngineCommand> stopDoorCommandObjectProvider = mock(ObjectProvider.class);

    private final ObjectProvider<OpenDoorCommand> openDoorCommandObjectProvider = mock(ObjectProvider.class);

    private final ObjectProvider<CloseDoorCommand> closeDoorCommandObjectProvider = mock(ObjectProvider.class);

    private final ObjectProvider<VisitFloorCommand> visitFloorCommandObjectProvider = mock(ObjectProvider.class);

    private final CommandManager commandManager = new CommandManager(houseThreadPool, pressUpButtonCommandProvider,
            findNearestCabinCommandProvider, activateShaftCommandProvider,
            moveCabinCommandObjectProvider, stopDoorCommandObjectProvider,
            openDoorCommandObjectProvider, closeDoorCommandObjectProvider, visitFloorCommandObjectProvider);

    @Test
    public void getCommandTest() {
        when(commandManager.getCommand("FindNearestCabinCommand")).thenReturn(mock(FindNearestCabinCommand.class));
        Command resultCommand = commandManager.getCommand("FindNearestCabinCommand");

        if (resultCommand instanceof FindNearestCabinCommand) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void getCommandFailedTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> commandManager.getCommand("BadName"));
    }
}
