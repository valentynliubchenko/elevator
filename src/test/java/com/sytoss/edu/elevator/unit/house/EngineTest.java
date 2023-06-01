package com.sytoss.edu.elevator.unit.house;

import com.sytoss.edu.elevator.bom.Engine;
import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.enums.EngineState;
import com.sytoss.edu.elevator.services.EngineListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class EngineTest {

    private final Engine engine = new Engine();

    @Test
    public void startUpwardsTest() {
        engine.start(Direction.UPWARDS);
        Assertions.assertEquals(EngineState.GOING_UP, engine.getEngineState());
    }

    @Test
    public void startDownwardsTest() {
        engine.start(Direction.DOWNWARDS);
        Assertions.assertEquals(EngineState.GOING_DOWN, engine.getEngineState());
    }

    @Test
    public void stopTest() {
        engine.stop();
        Assertions.assertEquals(EngineState.STAYING, engine.getEngineState());
    }

    @Test
    public void addListenerTest() {
        engine.addListener(mock(EngineListener.class));
        Assertions.assertEquals(1, engine.getEngineListeners().size());
    }
}