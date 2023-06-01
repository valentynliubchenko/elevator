package com.sytoss.edu.elevator.bom;

import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.enums.DoorState;
import com.sytoss.edu.elevator.commands.CommandManager;
import com.sytoss.edu.elevator.events.CabinPositionChangedEvent;
import com.sytoss.edu.elevator.events.DoorStateChangedEvent;
import com.sytoss.edu.elevator.services.CabinListener;
import com.sytoss.edu.elevator.services.OrderSequenceOfStopsListener;
import com.sytoss.edu.elevator.services.ShaftListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sytoss.edu.elevator.HouseThreadPool.*;
import static com.sytoss.edu.elevator.commands.Command.*;
import static com.sytoss.edu.elevator.commands.CommandManager.SHAFT_PARAM;

@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
public class ElevatorDriver extends Entity implements ShaftListener, CabinListener {

    private final CommandManager commandManager;

    private List<SequenceOfStops> orderSequenceOfStops = new ArrayList<>();

    private List<OrderSequenceOfStopsListener> orderSequenceOfStopsListeners = new ArrayList<>();

    public void addNewSequenceToOrder(int floorNumber, Direction direction) {
        SequenceOfStops sequenceOfStops = new SequenceOfStops();
        sequenceOfStops.setDirection(direction);
        sequenceOfStops.setStopFloors(new ArrayList<>(List.of(floorNumber)));
        orderSequenceOfStops.add(sequenceOfStops);
    }

    public void removeSequenceFromOrder() {
        if (orderSequenceOfStops.isEmpty()) {
            throw new IllegalStateException("Order sequence of stops is empty!");
        }
        orderSequenceOfStops.remove(0);
    }

    @Override
    public void handleCabinPositionChanged(CabinPositionChangedEvent event) {
        Shaft shaft = event.getShaft();

        HashMap<String, Object> params = new HashMap<>();
        params.put(SHAFT_PARAM, shaft);
        if (shaft.getSequenceOfStops().getStopFloors().contains(shaft.getCabinPosition())) {
            commandManager.getCommand(STOP_ENGINE_COMMAND).execute(params);
            commandManager.scheduleCommand(OPEN_DOOR_COMMAND, params, OPEN_DOOR_TIME_SLEEP);
        } else {
            commandManager.scheduleCommand(VISIT_FLOOR_COMMAND, params, VISIT_FLOOR_TIME_SLEEP);
        }
    }

    @Override
    public void handleDoorStateChanged(DoorStateChangedEvent event) {
        Shaft shaft = event.getShaft();

        HashMap<String, Object> params = new HashMap<>();
        params.put(SHAFT_PARAM, shaft);

        if (shaft.getCabin().getDoorState().equals(DoorState.OPENED)) {
            commandManager.scheduleCommand(CLOSE_DOOR_COMMAND, params, CLOSE_DOOR_TIME_SLEEP);
        } else {
            if (shaft.getSequenceOfStops().isLast(shaft.getCabinPosition())) {
                shaft.clearSequence();
                return;
            }

            commandManager.scheduleCommand(MOVE_CABIN_COMMAND, params, MOVE_CABIN_TIME_SLEEP);
        }
    }

    public void addListener(OrderSequenceOfStopsListener orderSequenceOfStopsListener) {
        orderSequenceOfStopsListeners.add(orderSequenceOfStopsListener);
    }
}