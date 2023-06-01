package com.sytoss.edu.elevator.services;

import com.sytoss.edu.elevator.events.EngineStateChangedEvent;
import com.sytoss.edu.elevator.repositories.ShaftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class EngineService implements EngineListener {

    private final ShaftRepository shaftRepository;

    @Override
    public void handleEngineStateChanged(EngineStateChangedEvent event) {
        shaftRepository.updateEngineStateById(event.getShaft().getId(), event.getShaft().getEngine().getEngineState());
        log.debug("Shaft with id [{}] updated engineState in DB to: [{}]", event.getShaft().getId(), event.getShaft().getEngine().getEngineState());
    }
}