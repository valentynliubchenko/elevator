package com.sytoss.edu.elevator.unit.house;

import com.sytoss.edu.elevator.bom.*;
import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.enums.DoorState;
import com.sytoss.edu.elevator.bom.enums.EngineState;
import com.sytoss.edu.elevator.commands.CommandManager;
import com.sytoss.edu.elevator.repositories.ShaftRepository;
import com.sytoss.edu.elevator.services.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class ShaftTest {

    private final ShaftRepository shaftRepository = mock(ShaftRepository.class);

    private final CabinService cabinService = spy(new CabinService(shaftRepository));

    private final ShaftService shaftService = spy(new ShaftService(shaftRepository));

    private final EngineService engineService = spy(new EngineService(shaftRepository));

    private final ElevatorDriver elevatorDriver = spy(new ElevatorDriver(mock(CommandManager.class)));

    private final Shaft shaft = new Shaft();

    @Test
    public void isMovingTest() {
        SequenceOfStops sequence = mock(SequenceOfStops.class);

        shaft.setSequenceOfStops(sequence);

        Assertions.assertTrue(shaft.isCabinMoving());
    }

    @Test
    public void isFreeTest() {
        Assertions.assertTrue(shaft.isFree());
    }

    @Test
    public void isEngineStaying() {
        Assertions.assertTrue(shaft.isEngineStaying());
    }

    @Test
    public void updateSequenceAddTest() {
        SequenceOfStops sequence = new SequenceOfStops();
        sequence.setStopFloors(List.of(5));

        SequenceOfStopsListener listener = mock(SequenceOfStopsListener.class);
        sequence.addListener(listener);

        shaft.updateSequence(sequence);

        Assertions.assertEquals(5, shaft.getSequenceOfStops().getStopFloors().get(0));
        verify(listener).handleSequenceOfStopsChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())));
    }

    @Test
    public void updateSequenceMergeTest() {
        shaft.setSequenceOfStops(null);
        SequenceOfStopsListener listener = mock(SequenceOfStopsListener.class);

        SequenceOfStops sequenceOfStops1 = new SequenceOfStops();
        sequenceOfStops1.setStopFloors(List.of(5));
        sequenceOfStops1.addListener(listener);
        shaft.setSequenceOfStops(sequenceOfStops1);

        SequenceOfStops sequenceOfStops2 = new SequenceOfStops();
        sequenceOfStops2.setStopFloors(List.of(3));
        sequenceOfStops2.addListener(listener);

        shaft.updateSequence(sequenceOfStops2);

        Assertions.assertEquals(List.of(3, 5), shaft.getSequenceOfStops().getStopFloors());
        verify(listener).handleSequenceOfStopsChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())));
    }

    @Test
    public void clearSequenceTest() {
        SequenceOfStops sequence = new SequenceOfStops();
        sequence.setStopFloors(List.of(1, 2, 3));
        sequence.setDirection(Direction.UPWARDS);

        shaft.setSequenceOfStops(sequence);
        Assertions.assertEquals(sequence, shaft.getSequenceOfStops());

        shaft.clearSequence();
        Assertions.assertNull(shaft.getSequenceOfStops());
    }

    @Test
    public void isSameDirectionTest() {
        SequenceOfStops sequence = new SequenceOfStops();
        sequence.setDirection(Direction.UPWARDS);

        shaft.setCabinPosition(1);
        shaft.setSequenceOfStops(sequence);

        Assertions.assertTrue(shaft.isSameDirection(Direction.UPWARDS, 5));

        shaft.setCabinPosition(6);
        Assertions.assertFalse(shaft.isSameDirection(Direction.UPWARDS, 5));
    }

    @Test
    public void addShaftListenerTest() {
        shaft.addShaftListener(mock(ShaftListener.class));
        Assertions.assertEquals(1, shaft.getShaftListeners().size());
    }

    @Test
    public void openCabinDoorTest() {

        Cabin cabin = spy(Cabin.class);
        shaft.setCabin(cabin);

        shaft.getCabin().addListener(cabinService);
        shaft.getCabin().addListener(elevatorDriver);
        SequenceOfStops sequenceOfStops = new SequenceOfStops();
        sequenceOfStops.addListener(shaftService);
        shaft.setSequenceOfStops(sequenceOfStops);

        shaft.openCabinDoor();

        verify(cabin).openDoor();
        verify(cabinService).handleDoorStateChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())));
        verify(shaftRepository).updateDoorStateById(shaft.getId(), DoorState.OPENED);
        verify(elevatorDriver).handleDoorStateChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())));
    }

    @Test
    public void closeCabinDoorTest() {
        Cabin cabin = spy(Cabin.class);
        shaft.setCabin(cabin);
        shaft.getCabin().addListener(cabinService);
        shaft.getCabin().addListener(elevatorDriver);
        shaft.setSequenceOfStops(mock(SequenceOfStops.class));

        shaft.closeCabinDoor();

        verify(cabin).closeDoor();
        verify(cabinService).handleDoorStateChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())));
        verify(shaftRepository).updateDoorStateById(shaft.getId(), DoorState.CLOSED);
        verify(elevatorDriver).handleDoorStateChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())));
    }

    @Test
    public void startEngineUpwardsTest() {
        Engine engine = spy(Engine.class);
        engine.addListener(engineService);
        shaft.setEngine(engine);

        shaft.startEngine(Direction.UPWARDS);

        verify(engine).start(Direction.UPWARDS);
        verify(engineService).handleEngineStateChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())));
        verify(shaftRepository).updateEngineStateById(shaft.getId(), EngineState.GOING_UP);
    }

    @Test
    public void startEngineDownwardsTest() {
        Engine engine = spy(Engine.class);
        engine.addListener(engineService);
        shaft.setEngine(engine);

        shaft.startEngine(Direction.DOWNWARDS);

        verify(engine).start(Direction.DOWNWARDS);
        verify(engineService).handleEngineStateChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())));
        verify(shaftRepository).updateEngineStateById(shaft.getId(), EngineState.GOING_DOWN);
    }

    @Test
    public void stopEngineTest() {
        Engine engine = spy(Engine.class);
        engine.addListener(engineService);
        shaft.setEngine(engine);

        shaft.stopEngine();

        verify(engine).stop();
        verify(engineService).handleEngineStateChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())));
        verify(shaftRepository).updateEngineStateById(shaft.getId(), EngineState.STAYING);
    }

    @Test
    public void setCabinPositionTest() {
        int floorNumber = 5;

        ShaftRepository shaftRepository = mock(ShaftRepository.class);
        ShaftService shaftService = spy(new ShaftService(shaftRepository));
        shaft.addShaftListener(shaftService);

        ElevatorDriver elevatorDriver = mock(ElevatorDriver.class);
        shaft.addShaftListener(elevatorDriver);

        shaft.setCabinPosition(floorNumber);

        verify(shaftService).handleCabinPositionChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())
                && arg.getShaft().getCabinPosition() == floorNumber));
        verify(shaftRepository).updateCabinPositionById(shaft.getId(), floorNumber);
        verify(elevatorDriver).handleCabinPositionChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())));
    }
}