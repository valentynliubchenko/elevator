package com.sytoss.edu.elevator.events;

import com.sytoss.edu.elevator.bom.Shaft;

public class EngineStateChangedEvent extends Event {

    public EngineStateChangedEvent(Shaft shaft) {
        super(shaft);
    }
}