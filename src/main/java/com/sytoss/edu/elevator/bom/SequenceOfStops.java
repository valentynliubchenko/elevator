package com.sytoss.edu.elevator.bom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.events.SequenceOfStopsChangedEvent;
import com.sytoss.edu.elevator.services.SequenceOfStopsListener;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SequenceOfStops extends Entity {

    private List<Integer> stopFloors;

    private Direction direction;

    @JsonIgnore
    private List<SequenceOfStopsListener> sequenceOfStopsListeners = new ArrayList<>();

    public boolean isLast(int floor) {
        return floor == stopFloors.get(stopFloors.size() - 1);
    }

    public boolean isFirst(int floor) {
        return stopFloors.get(0) == floor;
    }

    public void addListener(SequenceOfStopsListener sequenceOfStopsListener) {
        this.sequenceOfStopsListeners.add(sequenceOfStopsListener);
    }

    public void done(Long shaftId) {
        Shaft shaft = new Shaft();
        shaft.setId(shaftId);
        SequenceOfStopsChangedEvent sequenceOfStopsChangedEvent = new SequenceOfStopsChangedEvent(shaft);
        sequenceOfStopsListeners.forEach(listener -> listener.handleSequenceOfStopsChanged(sequenceOfStopsChangedEvent));
    }
}