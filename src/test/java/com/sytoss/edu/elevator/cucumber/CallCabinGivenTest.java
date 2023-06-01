package com.sytoss.edu.elevator.cucumber;

import com.sytoss.edu.elevator.IntegrationTest;
import com.sytoss.edu.elevator.TestContext;
import com.sytoss.edu.elevator.bom.SequenceOfStops;
import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.enums.DoorState;
import com.sytoss.edu.elevator.bom.enums.EngineState;
import com.sytoss.edu.elevator.bom.enums.OverWeightState;
import com.sytoss.edu.elevator.bom.house.House;
import com.sytoss.edu.elevator.commands.Command;
import com.sytoss.edu.elevator.commands.CommandManager;
import com.sytoss.edu.elevator.dto.HouseDTO;
import com.sytoss.edu.elevator.dto.ShaftDTO;
import com.sytoss.edu.elevator.utils.JsonUtil;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

public class CallCabinGivenTest extends IntegrationTest {

    @Given("shaft {int} in house {int} has free cabin and cabin position {int}")
    public void shaftWithIdAndEngineHasEngineStateAndShaftHasCurrentPosition(int shaftIndex, int houseIndex, int cabinPosition) {
        List<ShaftDTO> shaftDTOlist = getShaftDTOListByHouseId(getHouseId(houseIndex));
        shaftDTOlist.get(shaftIndex).setSequenceOfStops(null);
        shaftDTOlist.get(shaftIndex).setCabinPosition(cabinPosition);
        getShaftRepository().save(shaftDTOlist.get(shaftIndex));
    }

    @Given("shaft with index {int} has sequence of stops with floor/floors {intList} and Direction {string} and cabin position {int}")
    public void shaftWithIndexAndSequenceOfStopsAndDirectionAndCabinPosition(int shaftIndex, List<Integer> floors, String direction, int cabinPosition) {
        shaftWithIndexHasSequenceOfStopsWithFloorAndDirectionAndCabinPosition(shaftIndex, 0, floors, direction, cabinPosition);
    }

    @Given("All shaft are free and no sequence of stops in queue")
    public void allShaftFreeAndNoSequence() {
        houseWithNumOfFloorsAndShafts(0, 16, 2);
    }

    @Given("shaft {int} in house {int} has sequence of stops with floors {intList} and Direction {string} and cabin position {int}")
    public void shaftWithIndexHasSequenceOfStopsWithFloorAndDirectionAndCabinPosition(int shaftIndex, int houseIndex, List<Integer> floors, String direction, int cabinPosition) {
        SequenceOfStops sequence = new SequenceOfStops();
        sequence.setDirection(Direction.valueOf(direction));
        sequence.setStopFloors(floors);
        sequence.setId(0L);

        List<ShaftDTO> shaftDTOlist = getShaftDTOListByHouseId(getHouseId(houseIndex));
        shaftDTOlist.get(shaftIndex).setSequenceOfStops(JsonUtil.sequenceToStringInJSON(sequence));
        shaftDTOlist.get(shaftIndex).setCabinPosition(cabinPosition);
        getShaftRepository().save(shaftDTOlist.get(shaftIndex));
    }

    @Given("house {int} with numberOfFloors {int} and numberOfShafts {int}")
    public void houseWithNumOfFloorsAndShafts(int houseIndex, int numberOfFloors, int numberOfShafts) {
        HouseDTO houseDTO = HouseDTO.builder()
                .numberOfFloors(numberOfFloors)
                .numberOfShafts(numberOfShafts)
                .build();

        houseDTO.setShafts(IntStream.range(0, numberOfShafts).mapToObj(el -> ShaftDTO.builder()
                .houseDTO(houseDTO)
                .doorState(DoorState.CLOSED)
                .engineState(EngineState.STAYING)
                .overweightState(OverWeightState.NOT_OVERWEIGHT)
                .cabinPosition(1)
                .build()).toList());

        HouseDTO insertedHouseDTO = getHouseRepository().save(houseDTO);
        TestContext.getInstance().getHouseIds().put(houseIndex, insertedHouseDTO.getId());

        List<ShaftDTO> insertedShaftDTOs = getShaftRepository().saveAll(houseDTO.getShafts());
        TestContext.getInstance().getShaftIds().put(houseIndex, insertedShaftDTOs.stream().map(ShaftDTO::getId).toList());
    }

    @And("shaft with index {int} has free cabin and cabin position {int}")
    public void shaftWithIndexHasFreeCabinAndCabinPosition(int shaftIndex, int cabinPosition) {
        shaftWithIdAndEngineHasEngineStateAndShaftHasCurrentPosition(shaftIndex, 0, cabinPosition);
    }


    @And("shafts in house {int} are moving")
    public void houseIsMoving(int houseIndex) {
        House house = getHouseService().getHouse(getHouseId(houseIndex));
        for (Shaft shaft : house.getShafts()) {
            if (shaft.getSequenceOfStops() != null) {
                getHouseThreadPool().getFixedThreadPool().submit(() -> {
                    HashMap<String, Object> paramsActivateCommand = new HashMap<>();
                    paramsActivateCommand.put(CommandManager.SHAFT_PARAM, shaft);
                    paramsActivateCommand.put(CommandManager.FLOORS_PARAM, house.getFloors());
                    getCommandManager().getCommand(Command.MOVE_CABIN_COMMAND).execute(paramsActivateCommand);
                });
            }
        }
    }
}