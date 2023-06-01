package com.sytoss.edu.elevator.events;

import com.sytoss.edu.elevator.bom.Shaft;

public class SequenceOfStopsChangedEvent extends Event {

    public SequenceOfStopsChangedEvent(Shaft shaft) {
        super(shaft);
    }
}