package com.sytoss.edu.elevator.unit.services;

import com.sytoss.edu.elevator.bom.Cabin;
import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.enums.DoorState;
import com.sytoss.edu.elevator.events.DoorStateChangedEvent;
import com.sytoss.edu.elevator.repositories.ShaftRepository;
import com.sytoss.edu.elevator.services.CabinService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class CabinServiceTest {

    private final ShaftRepository shaftRepository = mock(ShaftRepository.class);

    private final CabinService cabinService = spy(new CabinService(shaftRepository));

    @Test
    public void handleDoorStateChangedTest() {
        Shaft shaft = mock(Shaft.class);
        when(shaft.getId()).thenReturn(2L);

        Cabin cabin = mock(Cabin.class);
        when(shaft.getCabin()).thenReturn(cabin);
        when(cabin.getDoorState()).thenReturn(DoorState.OPENED);

        DoorStateChangedEvent event = new DoorStateChangedEvent(shaft);

        cabinService.handleDoorStateChanged(event);

        verify(cabinService).handleDoorStateChanged(argThat(arg -> arg.getShaft().getId().equals(shaft.getId())));
        verify(shaftRepository).updateDoorStateById(shaft.getId(), DoorState.OPENED);
    }
}
