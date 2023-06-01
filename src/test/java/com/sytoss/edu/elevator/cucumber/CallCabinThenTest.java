package com.sytoss.edu.elevator.cucumber;

import com.sytoss.edu.elevator.IntegrationTest;
import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.house.House;
import com.sytoss.edu.elevator.dto.ShaftDTO;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static com.sytoss.edu.elevator.commands.CommandManager.SHAFT_PARAM;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
public class CallCabinThenTest extends IntegrationTest {

    @Then("Shaft with index {int} should have sequence of stops with floor {int} and direction {string}")
    public void shaftShouldCreateSequenceOfStopsWithFloorAndIdAndDirection(int shaftIndex, int floorRequested, String direction) {
        House house = getHouseService().getHouse(getHouseId(0));
        Assertions.assertEquals(floorRequested, house.getShafts().get(shaftIndex).getSequenceOfStops().getStopFloors().get(0));
        Assertions.assertEquals(Direction.valueOf(direction), house.getShafts().get(shaftIndex).getSequenceOfStops().getDirection());
    }

    @Then("Shaft with index {int} should not have sequence of stops")
    public void shaftShouldNotHaveSequenceOfStops(int shaftIndex) {
        House house = getHouseService().getHouse(getHouseId(0));
        Assertions.assertNull(house.getShafts().get(shaftIndex).getSequenceOfStops());
    }

    @Then("ElevatorDriver has sequence of stops with floor {int}")
    public void elevatorDriverHasSequenceOfStopsWithFloor(int floorNumber) {
        House house = getHouseService().getHouse(getHouseId(0));
        Assertions.assertEquals(floorNumber, house.getElevatorDriver().getOrderSequenceOfStops().get(0).getStopFloors().get(0));
    }

    @Then("Shaft with index {int} should have sequence of stops with floors {intList} and direction {string}")
    public void shaftWithIndexShouldHaveSequence(Integer shaftIndex, List<Integer> floors, String direction) {
        House house = getHouseService().getHouse(getHouseId(0));
        for (int i = 0; i < floors.size(); ++i) {
            Assertions.assertEquals(floors.get(i), house.getShafts().get(shaftIndex).getSequenceOfStops().getStopFloors().get(i));
            Assertions.assertEquals(Direction.valueOf(direction), house.getShafts().get(shaftIndex).getSequenceOfStops().getDirection());
        }
    }

    @Then("command/commands should have be invoked for shaft {int} in house {int}: {stringList} for floor/floors {intList}")
    public void commandsShouldHaveBeInvokedForShaftWithIndexOpenDoorCheckOverweightCloseDoorForFloor(
            int shaftIndex, int houseIndex, List<String> commands, List<Integer> stopFloors) {
        for (String command : commands) {
            verify(getCommandManager().getCommand(command), times(stopFloors.size())).execute(argThat((arg) -> {
                Shaft shaft = (Shaft) (arg.get(SHAFT_PARAM));
                return shaft.getId() == getShaftId(houseIndex, shaftIndex);
            }));
        }
    }

    @Then("commands should have be invoked for shaft with index {int}: {stringList} for floor/floors {intList}")
    public void commandsShouldHaveBeInvokedForShaftWithIndexOpenDoorCommandCloseDoorCommandForFloor(int shaftIndex, List<String> commands, List<Integer> stopFloors) {
        commandsShouldHaveBeInvokedForShaftWithIndexOpenDoorCheckOverweightCloseDoorForFloor(shaftIndex, 0, commands, stopFloors);
    }

    @And("shaft {int} in house {int} has cabin position {int}")
    public void shaftWithIndexHasCabinPosition(int shaftIndex, int houseIndex, int cabinPosition) {
        List<ShaftDTO> shaftDTOlist = getShaftDTOListByHouseId(getHouseId(houseIndex));
        Assertions.assertEquals(cabinPosition, shaftDTOlist.get(shaftIndex).getCabinPosition());
    }
}