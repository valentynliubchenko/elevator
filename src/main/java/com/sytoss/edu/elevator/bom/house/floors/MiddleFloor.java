package com.sytoss.edu.elevator.bom.house.floors;

import com.sytoss.edu.elevator.bom.house.buttons.FloorWithUpButton;
import com.sytoss.edu.elevator.bom.house.buttons.UpFloorButton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MiddleFloor extends Floor implements FloorWithUpButton {

    private final UpFloorButton upFloorButton;

    public MiddleFloor(int floorNumber, UpFloorButton upFloorButton) {
        super(floorNumber);
        this.upFloorButton = upFloorButton;
    }

    @Override
    public void pressUpButton() {
        log.info("MiddleFloor: pressUpButton ");
        upFloorButton.press(getFloorNumber());
    }
}
