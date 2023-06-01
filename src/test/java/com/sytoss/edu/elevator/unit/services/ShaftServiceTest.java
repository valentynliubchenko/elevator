package com.sytoss.edu.elevator.unit.services;

import com.sytoss.edu.elevator.bom.SequenceOfStops;
import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.dto.ShaftDTO;
import com.sytoss.edu.elevator.events.CabinPositionChangedEvent;
import com.sytoss.edu.elevator.events.SequenceOfStopsChangedEvent;
import com.sytoss.edu.elevator.repositories.ShaftRepository;
import com.sytoss.edu.elevator.services.ShaftService;
import com.sytoss.edu.elevator.utils.JsonUtil;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class ShaftServiceTest {

    private final ShaftRepository shaftRepository = mock(ShaftRepository.class);

    private final ShaftService shaftService = spy(new ShaftService(shaftRepository));

    @Test
    public void updateSequenceOfStopsFromDbTest() {
        long shaftId = 2L;
        String sequenceString = "{\"id\":123,\"stopFloors\":[1,2,3],\"direction\":\"UPWARDS\"}";

        ShaftDTO shaftDTO = mock(ShaftDTO.class);
        when(shaftRepository.getReferenceById(shaftId)).thenReturn(shaftDTO);
        when(shaftDTO.getSequenceOfStops()).thenReturn(sequenceString);

        Shaft shaft = mock(Shaft.class);
        when(shaft.getId()).thenReturn(shaftId);

        shaftService.updateSequenceOfStopsFromDb(shaft);

        verify(shaftRepository).getReferenceById(shaftId);

        verify(shaft).setSequenceOfStops(argThat((arg) -> {
            SequenceOfStops sequence = JsonUtil.stringJSONToSequenceOfStops(sequenceString);
            assert sequence != null;
            return sequence.getDirection().equals(arg.getDirection())
                    && sequence.getStopFloors().containsAll(arg.getStopFloors());
        }));
    }

    @Test
    public void updateSequenceIsThrownException() {
        Long shaftId = 2L;

        Shaft shaft = mock(Shaft.class);
        when(shaft.getId()).thenReturn(shaftId);

        when(shaftRepository.getReferenceById(shaftId)).thenThrow(EntityNotFoundException.class);
        Assertions.assertThrows(IllegalStateException.class, () -> shaftService.updateSequenceOfStopsFromDb(shaft));
    }

    @Test
    public void handleCabinPositionChangedTest() {
        int floorNumber = 5;

        Shaft shaft = mock(Shaft.class);
        when(shaft.getId()).thenReturn(2L);
        when(shaft.getCabinPosition()).thenReturn(floorNumber);

        CabinPositionChangedEvent event = new CabinPositionChangedEvent(shaft);

        shaftService.handleCabinPositionChanged(event);

        verify(shaftService).handleCabinPositionChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())
                && arg.getShaft().getCabinPosition() == floorNumber));
        verify(shaftRepository).updateCabinPositionById(shaft.getId(), floorNumber);
    }

    @Test
    public void handleSequenceOfStopsChangedTest() {
        Shaft shaft = mock(Shaft.class);
        when(shaft.getId()).thenReturn(2L);

        SequenceOfStops sequence = new SequenceOfStops();
        sequence.setId(123L);
        sequence.setStopFloors(List.of(1, 2, 3));
        sequence.setDirection(Direction.UPWARDS);
        when(shaft.getSequenceOfStops()).thenReturn(sequence);

        SequenceOfStopsChangedEvent event = new SequenceOfStopsChangedEvent(shaft);

        shaftService.handleSequenceOfStopsChanged(event);

        verify(shaftService).handleSequenceOfStopsChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())));

        String sequenceJson = "{\"id\":123,\"stopFloors\":[1,2,3],\"direction\":\"UPWARDS\"}";
        verify(shaftRepository).updateSequenceById(shaft.getId(), sequenceJson);
    }
}