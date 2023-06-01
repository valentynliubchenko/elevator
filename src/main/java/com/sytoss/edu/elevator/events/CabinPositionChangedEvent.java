package com.sytoss.edu.elevator.events;

import com.sytoss.edu.elevator.bom.Shaft;

public class CabinPositionChangedEvent extends Event {

    public CabinPositionChangedEvent(Shaft shaft) {
        super(shaft);
    }
}