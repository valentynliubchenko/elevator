package com.sytoss.edu.elevator.commands;

import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.enums.Direction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static com.sytoss.edu.elevator.HouseThreadPool.VISIT_FLOOR_TIME_SLEEP;
import static com.sytoss.edu.elevator.commands.CommandManager.DIRECTION_PARAM;
import static com.sytoss.edu.elevator.commands.CommandManager.SHAFT_PARAM;

@Slf4j
@Component
@RequiredArgsConstructor
public class MoveCabinCommand implements Command {

    private final CommandManager commandManager;

    @Override
    public void execute(HashMap<String, Object> params) {
        Shaft shaft = (Shaft) params.get(SHAFT_PARAM);
        params.put(DIRECTION_PARAM, Direction.UPWARDS);

        if (shaft.isEngineStaying()) {
            commandManager.getCommand(START_ENGINE_COMMAND).execute(params);
        }

        commandManager.scheduleCommand(VISIT_FLOOR_COMMAND, params, VISIT_FLOOR_TIME_SLEEP);
    }
}