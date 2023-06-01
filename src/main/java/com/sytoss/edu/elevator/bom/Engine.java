package com.sytoss.edu.elevator.bom;

import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.enums.EngineState;
import com.sytoss.edu.elevator.services.EngineListener;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
public class Engine extends Entity {

    private EngineState engineState = EngineState.STAYING;

    private List<EngineListener> engineListeners = new ArrayList<>();

    public void start(Direction direction) {
        switch (direction) {
            case UPWARDS -> engineState = EngineState.GOING_UP;
            case DOWNWARDS -> engineState = EngineState.GOING_DOWN;
            default -> stop();
        }
    }

    public void stop() {
        this.engineState = EngineState.STAYING;
    }

    public void addListener(EngineListener engineListener) {
        engineListeners.add(engineListener);
    }
}