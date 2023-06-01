package com.sytoss.edu.elevator.services;

import com.sytoss.edu.elevator.events.CabinPositionChangedEvent;

public interface ShaftListener {

    void handleCabinPositionChanged(CabinPositionChangedEvent event);
}