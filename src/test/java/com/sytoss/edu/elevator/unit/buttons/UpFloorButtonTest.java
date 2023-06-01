package com.sytoss.edu.elevator.unit.buttons;

import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.house.House;
import com.sytoss.edu.elevator.bom.house.buttons.UpFloorButton;
import com.sytoss.edu.elevator.commands.Command;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static com.sytoss.edu.elevator.commands.CommandManager.*;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
public class UpFloorButtonTest {

    @Mock
    private Command command;

    @Spy
    private House house;

    @Test
    public void pressTest() {
        int floorNumber = 5;

        UpFloorButton upFloorButton = new UpFloorButton(command, house);
        upFloorButton.press(floorNumber);

        HashMap<String, Object> params = new HashMap<>();

        params.put(FLOOR_NUMBER_PARAM, floorNumber);
        params.put(DIRECTION_PARAM, Direction.UPWARDS);
        params.put(HOUSE_PARAM, house);

        verify(command).execute(params);
    }
}
