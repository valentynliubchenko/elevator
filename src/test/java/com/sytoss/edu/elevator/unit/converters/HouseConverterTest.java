package com.sytoss.edu.elevator.unit.converters;

import com.sytoss.edu.elevator.bom.ElevatorDriver;
import com.sytoss.edu.elevator.bom.SequenceOfStops;
import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.enums.DoorState;
import com.sytoss.edu.elevator.bom.enums.EngineState;
import com.sytoss.edu.elevator.bom.enums.OverWeightState;
import com.sytoss.edu.elevator.bom.house.House;
import com.sytoss.edu.elevator.bom.house.HouseBuilder;
import com.sytoss.edu.elevator.commands.CommandManager;
import com.sytoss.edu.elevator.converters.HouseConverter;
import com.sytoss.edu.elevator.converters.ShaftConverter;
import com.sytoss.edu.elevator.dto.HouseDTO;
import com.sytoss.edu.elevator.dto.ShaftDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class HouseConverterTest {

    private final CommandManager commandManager = mock(CommandManager.class);

    private final HouseBuilder houseBuilder = new HouseBuilder(commandManager);

    private final ShaftConverter shaftConverter = mock(ShaftConverter.class);

    private final HouseConverter houseConverter = new HouseConverter(houseBuilder, shaftConverter);

    @Test
    public void toDTOTest() {
        ElevatorDriver elevatorDriver = mock(ElevatorDriver.class);
        House houseTmp = houseBuilder.build(2, 16);

        List<SequenceOfStops> order = new ArrayList<>();
        SequenceOfStops sequence = new SequenceOfStops();
        sequence.setStopFloors(List.of(1, 2, 3));
        sequence.setDirection(Direction.UPWARDS);
        sequence.setId(123L);
        order.add(sequence);

        houseTmp.setElevatorDriver(elevatorDriver);
        when(elevatorDriver.getOrderSequenceOfStops()).thenReturn(order);

        HouseDTO houseDTO = houseConverter.toDTO(houseTmp);

        Assertions.assertEquals(2, houseDTO.getNumberOfShafts());
        Assertions.assertEquals(16, houseDTO.getNumberOfFloors());
        Assertions.assertEquals("[{\"id\":123,\"stopFloors\":[1,2,3],\"direction\":\"UPWARDS\"}]", houseDTO.getOrderSequenceOfStops());
    }

    @Test
    public void fromDTOTest() {
        HouseDTO houseDTO = HouseDTO.builder().id(1L).numberOfFloors(16).numberOfShafts(1).orderSequenceOfStops(null).build();
        ShaftDTO shaftDTO = ShaftDTO.builder().id(1L).sequenceOfStops(null).cabinPosition(1).doorState(DoorState.CLOSED).engineState(EngineState.STAYING).overweightState(OverWeightState.NOT_OVERWEIGHT).houseDTO(houseDTO).build();

        House house = houseConverter.fromDTO(houseDTO, List.of(shaftDTO));

        Assertions.assertEquals(1, house.getId());
        Assertions.assertEquals(16, house.getFloors().size());
        Assertions.assertEquals(1, house.getShafts().size());
        verify(shaftConverter).fromDTO(shaftDTO);
    }
}
