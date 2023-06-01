package com.sytoss.edu.elevator.bom.house.buttons;

import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.house.House;
import com.sytoss.edu.elevator.commands.Command;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpFloorButton extends FloorButton {

    public UpFloorButton(Command command, House house) {
        super(command, house);
    }

    public void press(int floorNumber) {
        log.info("UpFloorButton: press floorNumber {} ", floorNumber);
        super.press(floorNumber, Direction.UPWARDS);
    }
}
