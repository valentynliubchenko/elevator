package com.sytoss.edu.elevator.unit.commands;

import com.sytoss.edu.elevator.bom.Cabin;
import com.sytoss.edu.elevator.bom.SequenceOfStops;
import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.enums.DoorState;
import com.sytoss.edu.elevator.commands.OpenDoorCommand;
import com.sytoss.edu.elevator.repositories.ShaftRepository;
import com.sytoss.edu.elevator.services.CabinService;
import com.sytoss.edu.elevator.services.ShaftService;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static com.sytoss.edu.elevator.commands.CommandManager.SHAFT_PARAM;
import static org.mockito.Mockito.*;

public class OpenDoorCommandTest {

    private final ShaftRepository shaftRepository = mock(ShaftRepository.class);

    private final ShaftService shaftService = mock(ShaftService.class);

    private final OpenDoorCommand openDoorCommand = new OpenDoorCommand(shaftService);

    private final CabinService cabinService = spy(new CabinService(shaftRepository));

    @Test
    public void executeTest() {
        long shaftId = 2L;
        Shaft shaft = spy(Shaft.class);
        shaft.setId(shaftId);
        Cabin cabin = spy(Cabin.class);
        shaft.setCabin(cabin);
        shaft.getCabin().addListener(cabinService);

        SequenceOfStops sequenceOfStops = new SequenceOfStops();
        sequenceOfStops.addListener(shaftService);
        shaft.setSequenceOfStops(sequenceOfStops);

        HashMap<String, Object> params = new HashMap<>();
        params.put(SHAFT_PARAM, shaft);

        when(cabin.getDoorState()).thenReturn(DoorState.OPENED);

        openDoorCommand.execute(params);

        verify(shaftService).updateSequenceOfStopsFromDb(shaft);
        verify(shaft).openCabinDoor();
        verify(shaftRepository).updateDoorStateById(shaftId, DoorState.OPENED);
    }
}
