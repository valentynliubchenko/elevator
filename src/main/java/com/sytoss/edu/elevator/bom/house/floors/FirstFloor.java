package com.sytoss.edu.elevator.bom.house.floors;

import com.sytoss.edu.elevator.bom.house.buttons.FloorWithUpButton;
import com.sytoss.edu.elevator.bom.house.buttons.UpFloorButton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FirstFloor extends Floor implements FloorWithUpButton {

    private final UpFloorButton upFloorButton;

    public FirstFloor(UpFloorButton upFloorButton) {
        super(1);
        this.upFloorButton = upFloorButton;
    }

    @Override
    public void pressUpButton() {
        log.info("FirstFloor: pressUpButton ");
        upFloorButton.press(getFloorNumber());
    }
}
