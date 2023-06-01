package com.sytoss.edu.elevator.commands;

import com.sytoss.edu.elevator.HouseThreadPool;
import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.house.House;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static com.sytoss.edu.elevator.commands.CommandManager.HOUSE_PARAM;

@Component
@Slf4j
@RequiredArgsConstructor
public class FindNearestCabinCommand implements Command {

    private final CommandManager commandManager;

    private final HouseThreadPool houseThreadPool;

    @Override
    public void execute(HashMap<String, Object> params) {
        House house = (House) params.get(HOUSE_PARAM);
        Shaft nearestCabin = house.findNearestCabin();

        if (nearestCabin == null) {
            return;
        }

        if (nearestCabin.isCabinMoving()) {
            house.updateSequence(nearestCabin);
            return;
        }

        house.updateSequence(nearestCabin);

        if (nearestCabin.getCabinPosition() > nearestCabin.getSequenceOfStops().getStopFloors().get(0)) {
            return;
        }

        if (nearestCabin.getSequenceOfStops().isFirst(nearestCabin.getCabinPosition())) {
            houseThreadPool.getFixedThreadPool().submit(() -> {
                HashMap<String, Object> paramsActivateCommand = new HashMap<>();
                paramsActivateCommand.put(CommandManager.SHAFT_PARAM, nearestCabin);
                log.info("[FindNearestCabin] start OpenDoorCommand for shaft with id {}", nearestCabin.getId());
                commandManager.getCommand(OPEN_DOOR_COMMAND).execute(paramsActivateCommand);
                log.info("[FindNearestCabin] finish OpenDoorCommand for shaft with id {}", nearestCabin.getId());
            });
            return;
        }

        houseThreadPool.getFixedThreadPool().submit(() -> {
            log.info("startMoveCabin: start threads for shaft with id {}", nearestCabin.getId());
            HashMap<String, Object> paramsActivateCommand = new HashMap<>();
            paramsActivateCommand.put(CommandManager.SHAFT_PARAM, nearestCabin);
            commandManager.getCommand(MOVE_CABIN_COMMAND).execute(paramsActivateCommand);
            log.info("startMoveCabin: finish threads for shaft with id {}", nearestCabin.getId());
        });
    }
}
