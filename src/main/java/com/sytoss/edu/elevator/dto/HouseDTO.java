package com.sytoss.edu.elevator.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Table(name = "HOUSE")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class HouseDTO {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NUMBER_OF_FLOORS")
    private int numberOfFloors;

    @Column(name = "NUMBER_OF_SHAFTS")
    private int numberOfShafts;

    @Column(name = "ORDER_SEQUENCE_OF_STOPS")
    private String orderSequenceOfStops;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "houseDTO")
    private List<ShaftDTO> shafts;
}
