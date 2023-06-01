package com.sytoss.edu.elevator.commands;

import com.sytoss.edu.elevator.bom.ElevatorDriver;
import com.sytoss.edu.elevator.bom.SequenceOfStops;
import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.house.House;
import com.sytoss.edu.elevator.services.ShaftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static com.sytoss.edu.elevator.commands.CommandManager.HOUSE_PARAM;

@Slf4j
@Component
@RequiredArgsConstructor
public class PressUpButtonCommand implements Command {

    private final CommandManager commandManager;

    private final ShaftService shaftService;

    @Override
    public void execute(HashMap<String, Object> params) {
        log.info("Start PressUpButton.execute COMMAND with params: {}", params);

        int numberFloor = (int) params.get(CommandManager.FLOOR_NUMBER_PARAM);
        Direction direction = (Direction) params.get(CommandManager.DIRECTION_PARAM);
        House house = (House) params.get(HOUSE_PARAM);

        house.addNewSequenceToOrder(numberFloor, direction);
        lastSequenceOfStops(house.getElevatorDriver()).addListener(shaftService);

        commandManager.getCommand(Command.FIND_NEAREST_CABIN_COMMAND).execute(params);
    }

    private SequenceOfStops lastSequenceOfStops(ElevatorDriver elevatorDriver) {
        return elevatorDriver.getOrderSequenceOfStops().get(elevatorDriver.getOrderSequenceOfStops().size() - 1);
    }
}