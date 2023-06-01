package com.sytoss.edu.elevator.bom.house.buttons;

import com.sytoss.edu.elevator.commands.Command;

import java.util.HashMap;

public abstract class CommandButton {

    private final Command command;

    CommandButton(Command command) {
        this.command = command;
    }

    protected void execute(HashMap<String, Object> params) {
        command.execute(params);
    }
}
