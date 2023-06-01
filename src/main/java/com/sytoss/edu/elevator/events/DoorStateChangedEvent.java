package com.sytoss.edu.elevator.events;

import com.sytoss.edu.elevator.bom.Shaft;

public class DoorStateChangedEvent extends Event {

    public DoorStateChangedEvent(Shaft shaft) {
        super(shaft);
    }
}
