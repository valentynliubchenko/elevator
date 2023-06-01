package com.sytoss.edu.elevator.unit.commands;

import com.sytoss.edu.elevator.bom.Engine;
import com.sytoss.edu.elevator.bom.SequenceOfStops;
import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.enums.EngineState;
import com.sytoss.edu.elevator.commands.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;

import static com.sytoss.edu.elevator.HouseThreadPool.VISIT_FLOOR_TIME_SLEEP;
import static com.sytoss.edu.elevator.HouseThreadPool.await;
import static com.sytoss.edu.elevator.commands.Command.VISIT_FLOOR_COMMAND;
import static com.sytoss.edu.elevator.commands.CommandManager.SHAFT_PARAM;
import static org.mockito.Mockito.*;

public class MoveCabinCommandTest {

    private final CommandManager commandManager = mock(CommandManager.class);

    private final MoveCabinCommand moveCabinCommand = new MoveCabinCommand(commandManager);

    @Test
    public void executeTest() {
        StartEngineCommand startEngineCommand = mock(StartEngineCommand.class);
        StopEngineCommand stopEngineCommand = mock(StopEngineCommand.class);

        Shaft shaft = spy(Shaft.class);
        Engine engine = new Engine();
        engine.setEngineState(EngineState.STAYING);
        SequenceOfStops sequence = new SequenceOfStops();
        sequence.setDirection(Direction.UPWARDS);
        sequence.setStopFloors(List.of(3, 4));

        shaft.setSequenceOfStops(sequence);
        shaft.setCabinPosition(1);
        shaft.setEngine(engine);

        HashMap<String, Object> params = new HashMap<>();
        params.put(SHAFT_PARAM, shaft);

        when(commandManager.getCommand(Command.START_ENGINE_COMMAND)).thenReturn(startEngineCommand);
        when(commandManager.getCommand(Command.VISIT_FLOOR_COMMAND)).thenReturn(mock(VisitFloorCommand.class));

        doAnswer(invocation -> {
            HashMap<String, Object> arg = invocation.getArgument(0);
            Shaft shaftArg = (Shaft) arg.get(SHAFT_PARAM);
            shaftArg.getEngine().setEngineState(EngineState.GOING_UP);
            return null;
        }).when(startEngineCommand).execute(params);

        doAnswer(invocation -> {
            HashMap<String, Object> arg = invocation.getArgument(0);
            Shaft shaftArg = (Shaft) arg.get(SHAFT_PARAM);
            shaftArg.getEngine().setEngineState(EngineState.STAYING);
            return null;
        }).when(stopEngineCommand).execute(params);

        moveCabinCommand.execute(params);
        await();

        verify(commandManager.getCommand(Command.START_ENGINE_COMMAND)).execute(Mockito.any());
        verify(commandManager).scheduleCommand(VISIT_FLOOR_COMMAND, params, VISIT_FLOOR_TIME_SLEEP);
    }
}