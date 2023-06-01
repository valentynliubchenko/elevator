package com.sytoss.edu.elevator.repositories;

import com.sytoss.edu.elevator.dto.HouseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface HouseRepository extends JpaRepository<HouseDTO, Long> {

    @Modifying
    @Query("UPDATE HouseDTO House SET House.orderSequenceOfStops = :orderSequenceOfStops WHERE House.id = :id")
    void updateOrderById(
            @Param("id") Long id,
            @Param("orderSequenceOfStops") String orderSequenceOfStops);
}
