package com.sytoss.edu.elevator.unit.converters;

import com.sytoss.edu.elevator.bom.SequenceOfStops;
import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.bom.enums.DoorState;
import com.sytoss.edu.elevator.bom.enums.EngineState;
import com.sytoss.edu.elevator.bom.enums.OverWeightState;
import com.sytoss.edu.elevator.converters.ShaftConverter;
import com.sytoss.edu.elevator.dto.HouseDTO;
import com.sytoss.edu.elevator.dto.ShaftDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.spy;

public class ShaftConverterTest {

    private final ShaftConverter shaftConverter = new ShaftConverter();

    @Test
    public void toDTOTest() {
        HouseDTO houseDTO = spy(HouseDTO.class);
        houseDTO.setId(138277872L);
        houseDTO.setNumberOfShafts(2);
        houseDTO.setNumberOfFloors(16);

        Shaft shaft = spy(Shaft.class);
        SequenceOfStops sequence = new SequenceOfStops();
        sequence.setStopFloors(List.of(1, 2, 3));
        sequence.setDirection(Direction.UPWARDS);
        sequence.setId(123L);
        shaft.setSequenceOfStops(sequence);
        ShaftDTO shaftDTO = shaftConverter.toDTO(shaft, houseDTO);

        Assertions.assertEquals(shaft.getEngine().getEngineState(), shaftDTO.getEngineState());
        Assertions.assertEquals(shaft.getCabin().getDoorState(), shaftDTO.getDoorState());
        Assertions.assertEquals(shaft.getCabin().getOverWeightState(), shaftDTO.getOverweightState());
        Assertions.assertEquals(shaft.getCabinPosition(), shaftDTO.getCabinPosition());
        Assertions.assertEquals(houseDTO.getId(), shaftDTO.getHouseDTO().getId());
        Assertions.assertEquals("{\"id\":123,\"stopFloors\":[1,2,3],\"direction\":\"UPWARDS\"}", shaftDTO.getSequenceOfStops());
    }

    @Test
    public void fromDTOTest() {
        ShaftDTO shaftDTO = ShaftDTO.builder().id(1L).sequenceOfStops(null).cabinPosition(1).doorState(DoorState.CLOSED).engineState(EngineState.STAYING).overweightState(OverWeightState.NOT_OVERWEIGHT).build();

        Shaft shaft = shaftConverter.fromDTO(shaftDTO);

        Assertions.assertEquals(1L, shaft.getId());
        Assertions.assertEquals(DoorState.CLOSED, shaft.getCabin().getDoorState());
        Assertions.assertEquals(EngineState.STAYING, shaft.getEngine().getEngineState());
        Assertions.assertEquals(OverWeightState.NOT_OVERWEIGHT, shaft.getCabin().getOverWeightState());
        Assertions.assertNull(shaft.getSequenceOfStops());
    }
}
