package com.sytoss.edu.elevator.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sytoss.edu.elevator.bom.SequenceOfStops;
import org.assertj.core.util.Strings;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;


public final class JsonUtil {

    public static String orderSequenceToStringInJSON(List<SequenceOfStops> orderSequenceOfStops) {
        if (isEmpty(orderSequenceOfStops)) {
            return null;
        }

        String json;

        try {
            json = new ObjectMapper().writeValueAsString(orderSequenceOfStops);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return json;
    }

    public static List<SequenceOfStops> stringJSONToOrderSequence(String json) {
        if (Strings.isNullOrEmpty(json)) {
            return new ArrayList<>();
        }

        List<SequenceOfStops> sequence = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            sequence = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, SequenceOfStops.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return sequence;
    }

    public static String sequenceToStringInJSON(SequenceOfStops sequenceOfStops) {
        if (sequenceOfStops == null) {
            return null;
        }

        String json;

        try {
            json = new ObjectMapper().writeValueAsString(sequenceOfStops);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return json;
    }

    public static SequenceOfStops stringJSONToSequenceOfStops(String json) {
        if (Strings.isNullOrEmpty(json)) {
            return null;
        }

        SequenceOfStops sequence = null;

        try {
            sequence = new ObjectMapper().readValue(json, SequenceOfStops.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return sequence;
    }
}
