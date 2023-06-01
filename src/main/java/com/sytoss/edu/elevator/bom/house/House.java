package com.sytoss.edu.elevator.bom.house;

import com.sytoss.edu.elevator.bom.ElevatorDriver;
import com.sytoss.edu.elevator.bom.Entity;
import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.house.floors.Floor;
import com.sytoss.edu.elevator.events.OrderSequenceOfStopsChangedEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Getter
@Setter
public class House extends Entity {

    private List<Shaft> shafts = new ArrayList<>();

    private List<Floor> floors = new ArrayList<>();

    private ElevatorDriver elevatorDriver;

    public Shaft findNearestCabin() {
        var orderSequenceOfStops = elevatorDriver.getOrderSequenceOfStops();

        List<Shaft> appropriateShafts = getFreeShafts();

        if (appropriateShafts.isEmpty()) {
            log.info("findNearestCabin: all cabin is busy!");
            appropriateShafts = shaftWithAppropriateDirection(orderSequenceOfStops.get(0).getDirection());
            if (appropriateShafts.isEmpty()) {
                log.info("findNearestCabin: appropriate cabin not found");
                return null;
            }
        }

        int firstStop = orderSequenceOfStops.get(0).getStopFloors().get(0);

        return appropriateShafts.stream().min(Comparator.comparingInt(shaft -> Math.abs(firstStop - shaft.getCabinPosition()))).orElse(null);
    }

    public Floor nextFloor(int currentFloorNumber) {
        Floor currentFloor = floors.stream()
                .filter(floor -> floor.getFloorNumber() == currentFloorNumber)
                .findAny()
                .orElse(null);

        return floors.get(floors.indexOf(currentFloor) + 1);
    }

    private List<Shaft> getFreeShafts() {
        return shafts.stream().filter(Shaft::isFree).toList();
    }

    private List<Shaft> shaftWithAppropriateDirection(Direction currentDirection) {
        var orderSequenceOfStops = elevatorDriver.getOrderSequenceOfStops();
        return shafts.stream().filter(shaft -> shaft.isSameDirection(currentDirection, orderSequenceOfStops.get(0).getStopFloors().get(0))).toList();
    }

    public void addNewSequenceToOrder(int floorNumber, Direction direction) {
        elevatorDriver.addNewSequenceToOrder(floorNumber, direction);
        fireOrderSequenceOfStops();
    }

    public void removeSequenceFromOrder() {
        elevatorDriver.removeSequenceFromOrder();
        fireOrderSequenceOfStops();
    }

    private void fireOrderSequenceOfStops() {
        OrderSequenceOfStopsChangedEvent event = new OrderSequenceOfStopsChangedEvent(this);
        elevatorDriver.getOrderSequenceOfStopsListeners().forEach(
                listener -> listener.handleOrderSequenceOfStopsChanged(event)
        );
    }

    public void updateSequence(Shaft shaft) {
        shaft.updateSequence(elevatorDriver.getOrderSequenceOfStops().get(0));
        removeSequenceFromOrder();
    }
}