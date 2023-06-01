package com.sytoss.edu.elevator.unit.services;

import com.sytoss.edu.elevator.bom.Engine;
import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.enums.EngineState;
import com.sytoss.edu.elevator.events.EngineStateChangedEvent;
import com.sytoss.edu.elevator.repositories.ShaftRepository;
import com.sytoss.edu.elevator.services.EngineService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class EngineServiceTest {

    private final ShaftRepository shaftRepository = mock(ShaftRepository.class);

    private final EngineService engineService = spy(new EngineService(shaftRepository));

    @Test
    public void handleEngineStateChangedTest() {
        Shaft shaft = mock(Shaft.class);
        when(shaft.getId()).thenReturn(2L);

        Engine engine = mock(Engine.class);
        when(shaft.getEngine()).thenReturn(engine);
        when(engine.getEngineState()).thenReturn(EngineState.STAYING);

        EngineStateChangedEvent event = new EngineStateChangedEvent(shaft);

        engineService.handleEngineStateChanged(event);

        verify(engineService).handleEngineStateChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())));
        verify(shaftRepository).updateEngineStateById(shaft.getId(), EngineState.STAYING);
    }
}
