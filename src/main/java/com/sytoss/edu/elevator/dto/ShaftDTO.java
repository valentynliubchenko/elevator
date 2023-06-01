package com.sytoss.edu.elevator.dto;

import com.sytoss.edu.elevator.bom.enums.DoorState;
import com.sytoss.edu.elevator.bom.enums.EngineState;
import com.sytoss.edu.elevator.bom.enums.OverWeightState;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "SHAFT")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ShaftDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SEQUENCE_OF_STOPS")
    private String sequenceOfStops;

    @Column(name = "CABIN_POSITION")
    private int cabinPosition;

    @Column(name = "DOOR_STATE")
    @Enumerated(value = EnumType.STRING)
    private DoorState doorState;

    @Column(name = "ENGINE_STATE")
    @Enumerated(value = EnumType.STRING)
    private EngineState engineState;

    @Column(name = "OVERWEIGHT_STATE")
    @Enumerated(value = EnumType.STRING)
    private OverWeightState overweightState;

    @ManyToOne
    @JoinColumn(name = "HOUSE_ID", referencedColumnName = "ID")
    private HouseDTO houseDTO;
}
