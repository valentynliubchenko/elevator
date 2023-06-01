package com.sytoss.edu.elevator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HouseThreadPoolConfiguration {

    @Bean("houseThreadPool")
    public HouseThreadPool getHouseThreadPool(
            @Value("${houseThreadPool.openDoorTimeSleep}") int openDoorSleepTime,
            @Value("${houseThreadPool.closeDoorTimeSleep}") int closeDoorSleepTime,
            @Value("${houseThreadPool.visitFloorTimeSleep}") int visitFloorSleepTime,
            @Value("${houseThreadPool.moveCabinTimeSleep}") int moveCabinSleepTime
    ) {
        return new HouseThreadPool(openDoorSleepTime, closeDoorSleepTime, visitFloorSleepTime, moveCabinSleepTime);
    }
}
