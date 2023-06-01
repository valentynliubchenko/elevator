package com.sytoss.edu.elevator.unit.floors;

import com.sytoss.edu.elevator.bom.house.buttons.UpFloorButton;
import com.sytoss.edu.elevator.bom.house.floors.MiddleFloor;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MiddleFloorTest {

    private final UpFloorButton upFloorButton = mock(UpFloorButton.class);

    @Test
    public void pressUpButton() {
        MiddleFloor middleFloor = new MiddleFloor(2, upFloorButton);
        middleFloor.pressUpButton();

        verify(upFloorButton).press(middleFloor.getFloorNumber());
    }
}
