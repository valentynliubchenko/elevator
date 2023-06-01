package com.sytoss.edu.elevator.services;

import com.sytoss.edu.elevator.events.EngineStateChangedEvent;

public interface EngineListener {

    void handleEngineStateChanged(EngineStateChangedEvent event);
}