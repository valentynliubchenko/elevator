package com.sytoss.edu.elevator.bom;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public abstract class Entity {

    private Long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
}