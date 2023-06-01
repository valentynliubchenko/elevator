package com.sytoss.edu.elevator.events;

import com.sytoss.edu.elevator.bom.house.House;
import lombok.Getter;

@Getter
public class OrderSequenceOfStopsChangedEvent extends Event {

    private final House house;

    public OrderSequenceOfStopsChangedEvent(House house) {
        super(null);
        this.house = house;
    }
}