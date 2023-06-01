package com.sytoss.edu.elevator.params;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class HouseParams {

    private int numberOfFloors;

    private int numberOfShafts;
}
