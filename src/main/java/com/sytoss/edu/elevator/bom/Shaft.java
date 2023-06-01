package com.sytoss.edu.elevator.bom;

import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.enums.EngineState;
import com.sytoss.edu.elevator.events.CabinPositionChangedEvent;
import com.sytoss.edu.elevator.events.DoorStateChangedEvent;
import com.sytoss.edu.elevator.events.EngineStateChangedEvent;
import com.sytoss.edu.elevator.events.SequenceOfStopsChangedEvent;
import com.sytoss.edu.elevator.services.ShaftListener;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Getter
@Setter
public class Shaft extends Entity {

    private int cabinPosition = 1;

    private SequenceOfStops sequenceOfStops;

    private Engine engine = new Engine();

    private Cabin cabin = new Cabin();

    private List<ShaftListener> shaftListeners = new ArrayList<>();

    public boolean isCabinMoving() {
        return !isFree();
    }

    public boolean isFree() {
        return sequenceOfStops == null;
    }

    public boolean isEngineStaying() {
        return engine.getEngineState().equals(EngineState.STAYING);
    }

    public synchronized void updateSequence(SequenceOfStops sequenceOfStops) {
        if (isFree()) {
            this.sequenceOfStops = sequenceOfStops;
        } else {
            ArrayList<Integer> stops = new ArrayList<>(this.sequenceOfStops.getStopFloors());
            stops.addAll(sequenceOfStops.getStopFloors());
            Collections.sort(stops);
            this.sequenceOfStops.setStopFloors(stops);
        }
        fireSequenceOfStops();

        log.info("Shaft with id {} and sequence of stops of found cabin: {}", getId(), sequenceOfStops.getStopFloors());
    }

    public void clearSequence() {
        this.sequenceOfStops.done(getId());
        this.sequenceOfStops = null;
    }

    public boolean isSameDirection(Direction direction, int currentPosition) {
        return cabinPosition <= currentPosition && direction.equals(this.sequenceOfStops.getDirection());
    }

    public void addShaftListener(ShaftListener shaftListener) {
        shaftListeners.add(shaftListener);
    }

    public void setCabinPosition(int currentFloor) {
        cabinPosition = currentFloor;
        fireCabinPosition();
    }

    public void openCabinDoor() {
        cabin.openDoor();
        fireDoorState();
    }

    public void closeCabinDoor() {
        cabin.closeDoor();
        fireDoorState();
    }

    public void startEngine(Direction direction) {
        engine.start(direction);
        fireEngineState();
    }

    public void stopEngine() {
        engine.stop();
        fireEngineState();
    }

    private void fireDoorState() {
        DoorStateChangedEvent doorStateChangedEvent = new DoorStateChangedEvent(this);
        cabin.getCabinListeners().forEach(listener -> listener.handleDoorStateChanged(doorStateChangedEvent));

        fireSequenceOfStops();
    }

    private void fireSequenceOfStops() {
        SequenceOfStopsChangedEvent sequenceOfStopsChangedEvent = new SequenceOfStopsChangedEvent(this);
        sequenceOfStops.getSequenceOfStopsListeners().forEach(listener -> listener.handleSequenceOfStopsChanged(sequenceOfStopsChangedEvent));
    }

    private void fireCabinPosition() {
        CabinPositionChangedEvent event = new CabinPositionChangedEvent(this);
        shaftListeners.forEach(shaftListener -> shaftListener.handleCabinPositionChanged(event));
    }

    private void fireEngineState() {
        EngineStateChangedEvent event = new EngineStateChangedEvent(this);
        engine.getEngineListeners().forEach(engineListener -> engineListener.handleEngineStateChanged(event));
    }
}