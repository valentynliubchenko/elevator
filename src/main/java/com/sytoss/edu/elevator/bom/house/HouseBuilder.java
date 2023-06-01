package com.sytoss.edu.elevator.bom.house;

import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.house.buttons.UpFloorButton;
import com.sytoss.edu.elevator.bom.house.floors.FirstFloor;
import com.sytoss.edu.elevator.bom.house.floors.MiddleFloor;
import com.sytoss.edu.elevator.commands.Command;
import com.sytoss.edu.elevator.commands.CommandManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HouseBuilder {

    private final CommandManager commandManager;

    public House build(int shaftCount, int floorCount) {
        House result = new House();
        for (int i = 0; i < shaftCount; i++) {
            result.getShafts().add(new Shaft());
        }

        result.getFloors().add(new FirstFloor(new UpFloorButton(commandManager.getCommand(Command.PRESS_UP_BUTTON), result)));
        for (int i = 1; i < floorCount; i++) {
            int floorNumber = i + 1;
            result.getFloors().add(new MiddleFloor(floorNumber, new UpFloorButton(commandManager.getCommand(Command.PRESS_UP_BUTTON), result)));
        }
        return result;
    }
}
