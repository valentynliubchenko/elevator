package com.sytoss.edu.elevator.unit.utils;

import com.sytoss.edu.elevator.bom.SequenceOfStops;
import com.sytoss.edu.elevator.bom.enums.Direction;
import com.sytoss.edu.elevator.utils.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class JsonUtilTest {

    @Test
    public void orderSequenceToStringInJSONTest() {
        List<SequenceOfStops> order = new ArrayList<>();
        SequenceOfStops sequence = new SequenceOfStops();
        sequence.setStopFloors(List.of(1, 2, 3));
        sequence.setDirection(Direction.UPWARDS);
        sequence.setId(123L);
        order.add(sequence);

        String json = JsonUtil.orderSequenceToStringInJSON(order);

        Assertions.assertEquals("[{\"id\":123,\"stopFloors\":[1,2,3],\"direction\":\"UPWARDS\"}]", json);
    }

    @Test
    public void stringJSONToOrderSequenceTest() {
        String json = "[{\"id\":123,\"stopFloors\":[1,2,3],\"direction\":\"UPWARDS\"}]";

        List<SequenceOfStops> order = JsonUtil.stringJSONToOrderSequence(json);

        SequenceOfStops sequence = order.get(0);

        Assertions.assertEquals(123, sequence.getId());
        Assertions.assertEquals(Direction.UPWARDS, sequence.getDirection());
        Assertions.assertEquals(List.of(1, 2, 3), sequence.getStopFloors());
    }

    @Test
    public void sequenceToStringInJSONTest() {
        SequenceOfStops sequence = new SequenceOfStops();
        sequence.setStopFloors(List.of(1, 2, 3));
        sequence.setDirection(Direction.UPWARDS);
        sequence.setId(123L);

        String json = JsonUtil.sequenceToStringInJSON(sequence);

        Assertions.assertEquals("{\"id\":123,\"stopFloors\":[1,2,3],\"direction\":\"UPWARDS\"}", json);
    }

    @Test
    public void stringJSONToSequenceOfStops() {
        String json = "{\"id\":123,\"stopFloors\":[1,2,3],\"direction\":\"UPWARDS\"}";

        SequenceOfStops sequence = JsonUtil.stringJSONToSequenceOfStops(json);

        Assertions.assertEquals(123L, sequence.getId());
        Assertions.assertEquals(Direction.UPWARDS, sequence.getDirection());
        Assertions.assertEquals(List.of(1, 2, 3), sequence.getStopFloors());
    }
}
