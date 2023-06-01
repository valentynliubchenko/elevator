package com.sytoss.edu.elevator.unit.commands;

import com.sytoss.edu.elevator.HouseThreadPool;
import com.sytoss.edu.elevator.bom.ElevatorDriver;
import com.sytoss.edu.elevator.bom.SequenceOfStops;
import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.house.House;
import com.sytoss.edu.elevator.commands.*;
import com.sytoss.edu.elevator.repositories.ShaftRepository;
import com.sytoss.edu.elevator.services.HouseService;
import com.sytoss.edu.elevator.services.ShaftService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

import static com.sytoss.edu.elevator.commands.CommandManager.HOUSE_PARAM;
import static com.sytoss.edu.elevator.commands.CommandManager.SHAFT_PARAM;
import static org.mockito.Mockito.*;

public class FindNearestCabinCommandTest {

    private final House house = spy(House.class);

    private final CommandManager commandManager = mock(CommandManager.class);

    private final ElevatorDriver elevatorDriver = mock(ElevatorDriver.class);

    private final ShaftRepository shaftRepository = mock(ShaftRepository.class);

    private final HouseThreadPool houseThreadPool = mock(HouseThreadPool.class);

    private final MoveCabinCommand moveCabinCommand = mock(MoveCabinCommand.class);

    private final OpenDoorCommand openDoorCommand = mock(OpenDoorCommand.class);

    private final ShaftService shaftService = spy(new ShaftService(shaftRepository));

    private final HouseService houseService = mock(HouseService.class);

    private final FindNearestCabinCommand findNearestCabinCommand = new FindNearestCabinCommand(commandManager, houseThreadPool);

    private void setCommandMocks() {
        when(commandManager.getCommand(Command.MOVE_CABIN_COMMAND)).thenReturn(moveCabinCommand);
        when(commandManager.getCommand(Command.OPEN_DOOR_COMMAND)).thenReturn(openDoorCommand);
    }

    @BeforeEach
    void setupHouse() {
        Long houseId = 2L;
        house.setId(houseId);
        house.setElevatorDriver(elevatorDriver);
    }

    @Test
    public void executeTest() {
        SequenceOfStops sequenceInOrder = new SequenceOfStops();
        sequenceInOrder.setStopFloors(List.of(5));
        sequenceInOrder.setDirection(Direction.UPWARDS);

        when(elevatorDriver.getOrderSequenceOfStops()).thenReturn(List.of(sequenceInOrder));
        when(elevatorDriver.getOrderSequenceOfStopsListeners()).thenReturn(List.of(houseService));

        SequenceOfStops sequence = new SequenceOfStops();
        sequence.setDirection(Direction.UPWARDS);
        sequence.setStopFloors(List.of(2, 3, 4));
        sequence.addListener(shaftService);

        Shaft shaft = spy(new Shaft());
        shaft.setSequenceOfStops(sequence);
        house.getShafts().add(shaft);

        when(houseThreadPool.getFixedThreadPool()).thenReturn(Executors.newScheduledThreadPool(4));

        setCommandMocks();

        HashMap<String, Object> params = new HashMap<>();
        params.put(HOUSE_PARAM, house);

        findNearestCabinCommand.execute(params);
        await();

        verify(house).findNearestCabin();
        verify(shaft).isCabinMoving();
        verify(house).updateSequence(argThat(arg -> arg.getId().equals(shaft.getId())));

        verify(shaftService).handleSequenceOfStopsChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())));
        String sequenceOfStopsToDb = "{\"id\":" + sequence.getId() + ",\"stopFloors\":[2,3,4,5],\"direction\":\"UPWARDS\"}";
        verify(shaftRepository).updateSequenceById(shaft.getId(), sequenceOfStopsToDb);

        verify(houseService).handleOrderSequenceOfStopsChanged(argThat(arg -> arg.getHouse().getId().equals(house.getId())));
        verify(commandManager.getCommand(Command.MOVE_CABIN_COMMAND), never()).execute(any());
    }

    @Test
    public void executeShaftIsNullTest() {
        when(commandManager.getCommand(Command.MOVE_CABIN_COMMAND)).thenReturn(mock(MoveCabinCommand.class));
        when(elevatorDriver.getOrderSequenceOfStops()).thenReturn(List.of(new SequenceOfStops()));

        HashMap<String, Object> params = new HashMap<>();
        params.put(HOUSE_PARAM, house);

        findNearestCabinCommand.execute(params);

        verify(commandManager.getCommand(Command.MOVE_CABIN_COMMAND), times(0)).execute(Mockito.any());
    }

    @Test
    public void executeMovingDownTest() {
        SequenceOfStops sequenceInOrder = new SequenceOfStops();
        sequenceInOrder.setStopFloors(List.of(5));
        sequenceInOrder.setDirection(Direction.DOWNWARDS);
        sequenceInOrder.addListener(shaftService);

        when(elevatorDriver.getOrderSequenceOfStops()).thenReturn(List.of(sequenceInOrder));
        when(elevatorDriver.getOrderSequenceOfStopsListeners()).thenReturn(List.of(houseService));

        Shaft shaft = spy(new Shaft());
        shaft.setCabinPosition(6);
        house.getShafts().add(shaft);

        setCommandMocks();

        when(houseThreadPool.getFixedThreadPool()).thenReturn(Executors.newScheduledThreadPool(4));

        HashMap<String, Object> params = new HashMap<>();
        params.put(HOUSE_PARAM, house);

        findNearestCabinCommand.execute(params);
        await();

        verify(house).findNearestCabin();
        verify(shaft).isCabinMoving();
        verify(house).updateSequence(shaft);

        verify(shaftService).handleSequenceOfStopsChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())));
        String sequenceOfStopsToDb = "{\"id\":" + sequenceInOrder.getId() + ",\"stopFloors\":[5],\"direction\":\"DOWNWARDS\"}";
        verify(shaftRepository).updateSequenceById(shaft.getId(), sequenceOfStopsToDb);

        verify(houseService).handleOrderSequenceOfStopsChanged(argThat(arg -> arg.getHouse().getId().equals(house.getId())));

        verify(commandManager.getCommand(Command.MOVE_CABIN_COMMAND), never()).execute(any());
        verify(commandManager.getCommand(Command.OPEN_DOOR_COMMAND), never()).execute(any());
    }

    @Test
    public void executeCabinOnTheSameFloorTest() {
        int floorNumber = 5;

        SequenceOfStops sequenceInOrder = new SequenceOfStops();
        sequenceInOrder.setStopFloors(List.of(floorNumber));
        sequenceInOrder.setDirection(Direction.UPWARDS);
        sequenceInOrder.addListener(shaftService);

        when(elevatorDriver.getOrderSequenceOfStops()).thenReturn(List.of(sequenceInOrder));
        when(elevatorDriver.getOrderSequenceOfStopsListeners()).thenReturn(List.of(houseService));

        Shaft shaft = spy(new Shaft());
        shaft.setCabinPosition(floorNumber);
        house.getShafts().add(shaft);

        setCommandMocks();

        when(houseThreadPool.getFixedThreadPool()).thenReturn(Executors.newScheduledThreadPool(4));

        HashMap<String, Object> params = new HashMap<>();
        params.put(HOUSE_PARAM, house);

        findNearestCabinCommand.execute(params);
        await();

        verify(house).findNearestCabin();
        verify(shaft).isCabinMoving();
        verify(house).updateSequence(shaft);

        verify(shaftService).handleSequenceOfStopsChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())));
        String sequenceOfStopsToDb = "{\"id\":" + sequenceInOrder.getId() + ",\"stopFloors\":[" + floorNumber + "],\"direction\":\"UPWARDS\"}";
        verify(shaftRepository).updateSequenceById(shaft.getId(), sequenceOfStopsToDb);

        verify(houseService).handleOrderSequenceOfStopsChanged(argThat(arg -> arg.getHouse().getId().equals(house.getId())));

        verify(commandManager.getCommand(Command.MOVE_CABIN_COMMAND), never()).execute(any());
        verify(commandManager.getCommand(Command.OPEN_DOOR_COMMAND), times(1))
                .execute(argThat((arg) -> ((Shaft) (arg.get(SHAFT_PARAM))).getId().equals(shaft.getId())));
    }

    @Test
    public void executeIsCabinMovingTest() {
        int floorNumberInOrder = 7;
        int floorNumberInShaft = 4;
        int cabinPosition = 3;

        SequenceOfStops sequenceInOrder = new SequenceOfStops();
        sequenceInOrder.setStopFloors(List.of(floorNumberInOrder));
        sequenceInOrder.setDirection(Direction.UPWARDS);
        when(elevatorDriver.getOrderSequenceOfStops()).thenReturn(List.of(sequenceInOrder));
        when(elevatorDriver.getOrderSequenceOfStopsListeners()).thenReturn(List.of(houseService));

        SequenceOfStops sequenceInShaft = new SequenceOfStops();
        sequenceInShaft.setStopFloors(List.of(floorNumberInShaft));
        sequenceInShaft.setDirection(Direction.UPWARDS);
        sequenceInShaft.addListener(shaftService);

        Shaft shaft = spy(new Shaft());
        shaft.setCabinPosition(cabinPosition);
        shaft.setSequenceOfStops(sequenceInShaft);
        house.getShafts().add(shaft);

        setCommandMocks();

        when(houseThreadPool.getFixedThreadPool()).thenReturn(Executors.newScheduledThreadPool(4));

        HashMap<String, Object> params = new HashMap<>();
        params.put(HOUSE_PARAM, house);

        findNearestCabinCommand.execute(params);
        await();

        verify(house).findNearestCabin();
        verify(shaft).isCabinMoving();
        verify(house).updateSequence(shaft);

        verify(shaftService).handleSequenceOfStopsChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())));
        String sequenceOfStopsToDb = "{\"id\":" + sequenceInShaft.getId() + ",\"stopFloors\":[" + floorNumberInShaft + "," + floorNumberInOrder + "],\"direction\":\"UPWARDS\"}";
        verify(shaftRepository).updateSequenceById(shaft.getId(), sequenceOfStopsToDb);

        verify(houseService).handleOrderSequenceOfStopsChanged(argThat(arg -> arg.getHouse().getId().equals(house.getId())));

        verify(commandManager.getCommand(Command.MOVE_CABIN_COMMAND), never()).execute(any());
        verify(commandManager.getCommand(Command.OPEN_DOOR_COMMAND), never()).execute(any());
    }

    private void await() {
        int time = 1000;
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}