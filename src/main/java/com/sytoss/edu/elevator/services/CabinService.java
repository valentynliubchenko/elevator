package com.sytoss.edu.elevator.services;

import com.sytoss.edu.elevator.events.DoorStateChangedEvent;
import com.sytoss.edu.elevator.repositories.ShaftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CabinService implements CabinListener {

    private final ShaftRepository shaftRepository;

    @Override
    public void handleDoorStateChanged(DoorStateChangedEvent event) {
        shaftRepository.updateDoorStateById(event.getShaft().getId(), event.getShaft().getCabin().getDoorState());
        log.debug("Shaft with id [{}] updated door state in DB to: [{}]", event.getShaft().getId(), event.getShaft().getCabin().getDoorState());
    }
}