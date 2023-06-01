package com.sytoss.edu.elevator.cucumber;

import com.sytoss.edu.elevator.IntegrationTest;
import com.sytoss.edu.elevator.TestContext;
import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.house.House;
import com.sytoss.edu.elevator.utils.JsonUtil;
import io.cucumber.java.en.When;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static com.sytoss.edu.elevator.commands.Command.MOVE_CABIN_COMMAND;
import static com.sytoss.edu.elevator.commands.CommandManager.SHAFT_PARAM;

public class CallCabinWhenTest extends IntegrationTest {

    @When("passenger on floor {int} presses UpFloorButton with direction {string}")
    public void passengerOnFloorPressesUpFloorButtonWithDirection(int floorNumber, String direction) {
        passengerOnFloorPressesUpFloorButton(0, floorNumber);
    }

    @When("call process findNearestCabin for floor {int} with direction {string}")
    public void callProcessFindNearestCabinForFloor(int floor, String direction) {
        House house = getHouseService().getHouse(getHouseId(0));
        house.addNewSequenceToOrder(floor, Direction.valueOf(direction));

        getHouseRepository().updateOrderById(house.getId(), JsonUtil.orderSequenceToStringInJSON(house.getElevatorDriver().getOrderSequenceOfStops()));

        Shaft shaft = house.findNearestCabin();
        if (shaft != null) {
            house.updateSequence(shaft);
            getShaftRepository().updateSequenceById(shaft.getId(), JsonUtil.sequenceToStringInJSON(shaft.getSequenceOfStops()));
        }
    }

    @When("start cabin with index {int} moving sequence of stops to")
    public void startCabinWithIndexMovingSequenceOfStopsTo(int shaftIndex) {
        House house = getHouseService().getHouse(getHouseId(0));
        HashMap<String, Object> paramsExec = new HashMap<>();
        paramsExec.put(SHAFT_PARAM, house.getShafts().get(shaftIndex));
        getCommandManager().getCommand(MOVE_CABIN_COMMAND).execute(paramsExec);
        await(house.getShafts().get(shaftIndex).getSequenceOfStops().getStopFloors().get(house.getShafts().get(shaftIndex).getSequenceOfStops().getStopFloors().size() - 1));
    }

    @When("passenger in house {int} presses UpFloorButton on floor {int}")
    public void passengerOnFloorPressesUpFloorButton(int houseIndex, int floorNumber) {
        long houseId = getHouseId(houseIndex);
        String url = "/api/house/" + houseId + "/floorButton/" + floorNumber + "/up";
        ResponseEntity<String> response = doPost(url, null, String.class);
        TestContext.getInstance().setResponse(response);
        await(floorNumber);
    }
}
