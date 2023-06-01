package com.sytoss.edu.elevator.unit.floors;

import com.sytoss.edu.elevator.bom.house.buttons.UpFloorButton;
import com.sytoss.edu.elevator.bom.house.floors.FirstFloor;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FirstFloorTest {

    private final UpFloorButton upFloorButton = mock(UpFloorButton.class);

    @Test
    public void pressUpButton() {
        FirstFloor firstFloor = new FirstFloor(upFloorButton);
        firstFloor.pressUpButton();

        verify(upFloorButton).press(firstFloor.getFloorNumber());
    }
}
