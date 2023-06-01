package com.sytoss.edu.elevator.unit.controller;

import com.sytoss.edu.elevator.controller.FloorController;
import com.sytoss.edu.elevator.services.FloorService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FloorControllerTest {

    private final FloorService floorService = mock(FloorService.class);

    private final FloorController floorController = new FloorController(floorService);

    @Test
    public void goUpCabinRequestTest() {
        floorController.goUpCabinRequest(0L, 5);

        verify(floorService).goUpCabinRequest(0L, 5);
    }
}
