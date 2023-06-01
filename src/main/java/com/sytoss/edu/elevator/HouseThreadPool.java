package com.sytoss.edu.elevator;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class HouseThreadPool {

    public static int OPEN_DOOR_TIME_SLEEP;

    public static int CLOSE_DOOR_TIME_SLEEP;

    public static int VISIT_FLOOR_TIME_SLEEP;

    public static int MOVE_CABIN_TIME_SLEEP;

    private final ScheduledExecutorService fixedThreadPool = Executors.newScheduledThreadPool(4);

    public HouseThreadPool(int openDoorTimeSleep, int closeDoorTimeSleep, int visitFloorTimeSleep, int moveCabinTimeSleep) {
        OPEN_DOOR_TIME_SLEEP = openDoorTimeSleep;
        CLOSE_DOOR_TIME_SLEEP = closeDoorTimeSleep;
        VISIT_FLOOR_TIME_SLEEP = visitFloorTimeSleep;
        MOVE_CABIN_TIME_SLEEP = moveCabinTimeSleep;
    }

    public static void await() {
        final int AWAIT_TIME = 50;
        try {
            Thread.sleep(AWAIT_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized ScheduledExecutorService getFixedThreadPool() {
        return fixedThreadPool;
    }
}