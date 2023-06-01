package com.sytoss.edu.elevator.unit.commands;

import com.sytoss.edu.elevator.bom.Engine;
import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.enums.EngineState;
import com.sytoss.edu.elevator.commands.StartEngineCommand;
import com.sytoss.edu.elevator.repositories.ShaftRepository;
import com.sytoss.edu.elevator.services.EngineService;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static com.sytoss.edu.elevator.commands.CommandManager.DIRECTION_PARAM;
import static com.sytoss.edu.elevator.commands.CommandManager.SHAFT_PARAM;
import static org.mockito.Mockito.*;

public class StartEngineCommandTest {

    private final ShaftRepository shaftRepository = mock(ShaftRepository.class);

    private final EngineService engineService = spy(new EngineService(shaftRepository));

    private final StartEngineCommand startEngineCommand = new StartEngineCommand();

    @Test
    public void executeTest() {
        long shaftId = 123L;

        Shaft shaft = spy(Shaft.class);
        shaft.setId(shaftId);

        Engine engine = spy(Engine.class);
        engine.addListener(engineService);
        when(engine.getEngineState()).thenReturn(EngineState.GOING_UP);
        shaft.setEngine(engine);

        HashMap<String, Object> params = new HashMap<>();
        params.put(SHAFT_PARAM, shaft);
        params.put(DIRECTION_PARAM, Direction.UPWARDS);

        startEngineCommand.execute(params);

        verify(shaft).startEngine(Direction.UPWARDS);
        verify(engine).start(Direction.UPWARDS);
        verify(engineService).handleEngineStateChanged(argThat(arg -> arg.getShaft().getId().equals(shaftId)));
        verify(shaftRepository).updateEngineStateById(shaftId, EngineState.GOING_UP);
    }
}