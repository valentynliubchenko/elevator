package com.sytoss.edu.elevator.bom.house.buttons;

import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.house.House;
import com.sytoss.edu.elevator.commands.Command;
import com.sytoss.edu.elevator.commands.CommandManager;

import java.util.HashMap;

public abstract class FloorButton extends CommandButton {

    private final House house;

    protected FloorButton(Command command, House house) {
        super(command);
        this.house = house;
    }

    public void press(int floorNumber, Direction direction) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(CommandManager.HOUSE_PARAM, house);
        params.put(CommandManager.FLOOR_NUMBER_PARAM, floorNumber);
        params.put(CommandManager.DIRECTION_PARAM, direction);
        execute(params);
    }
}