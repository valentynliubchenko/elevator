package com.sytoss.edu.elevator.services;

import com.sytoss.edu.elevator.events.OrderSequenceOfStopsChangedEvent;

public interface OrderSequenceOfStopsListener {

    void handleOrderSequenceOfStopsChanged(OrderSequenceOfStopsChangedEvent event);
}