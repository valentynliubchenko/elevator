package com.sytoss.edu.elevator.converters;

import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.dto.HouseDTO;
import com.sytoss.edu.elevator.dto.ShaftDTO;
import com.sytoss.edu.elevator.utils.JsonUtil;
import org.springframework.stereotype.Component;

@Component
public class ShaftConverter {

    public ShaftDTO toDTO(Shaft shaft, HouseDTO houseDTO) {
        return ShaftDTO.builder().cabinPosition(shaft.getCabinPosition()).doorState(shaft.getCabin()
                        .getDoorState()).engineState(shaft.getEngine().getEngineState())
                .overweightState(shaft.getCabin().getOverWeightState()).houseDTO(houseDTO)
                .sequenceOfStops(JsonUtil.sequenceToStringInJSON(shaft.getSequenceOfStops())).build();
    }

    public Shaft fromDTO(ShaftDTO shaftDTO) {
        Shaft shaft = new Shaft();
        shaft.setId(shaftDTO.getId());
        shaft.setCabinPosition(shaftDTO.getCabinPosition());
        shaft.getEngine().setEngineState(shaftDTO.getEngineState());
        shaft.getCabin().setDoorState(shaftDTO.getDoorState());
        shaft.getCabin().setOverWeightState(shaftDTO.getOverweightState());
        shaft.setSequenceOfStops(JsonUtil.stringJSONToSequenceOfStops(shaftDTO.getSequenceOfStops()));

        return shaft;
    }
}
