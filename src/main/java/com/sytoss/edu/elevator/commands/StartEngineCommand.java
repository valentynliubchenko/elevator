package com.sytoss.edu.elevator.commands;

import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.enums.Direction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartEngineCommand implements Command {

    @Override
    public void execute(HashMap<String, Object> params) {
        Shaft shaft = (Shaft) params.get(CommandManager.SHAFT_PARAM);
        shaft.startEngine((Direction) params.get(CommandManager.DIRECTION_PARAM));

        log.info("Engine in shaft with id [{}] has engine state: [{}]", shaft.getId(), shaft.getEngine().getEngineState());
    }
}