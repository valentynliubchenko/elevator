package com.sytoss.edu.elevator.unit.house;

import com.sytoss.edu.elevator.bom.house.House;
import com.sytoss.edu.elevator.bom.house.HouseBuilder;
import com.sytoss.edu.elevator.commands.CommandManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class HouseBuilderTest {

    private final CommandManager commandManager = mock(CommandManager.class);

    private final HouseBuilder houseBuilder = new HouseBuilder(commandManager);

    @Test
    public void buildTest() {
        House resultHouse = houseBuilder.build(2, 16);

        Assertions.assertEquals(2, resultHouse.getShafts().size());
        Assertions.assertEquals(16, resultHouse.getFloors().size());
    }
}
