package com.sytoss.edu.elevator.services;

import com.sytoss.edu.elevator.bom.SequenceOfStops;
import com.sytoss.edu.elevator.bom.Shaft;
import com.sytoss.edu.elevator.dto.ShaftDTO;
import com.sytoss.edu.elevator.events.CabinPositionChangedEvent;
import com.sytoss.edu.elevator.events.SequenceOfStopsChangedEvent;
import com.sytoss.edu.elevator.repositories.ShaftRepository;
import com.sytoss.edu.elevator.utils.JsonUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShaftService implements ShaftListener, SequenceOfStopsListener {

    private final ShaftRepository shaftRepository;

    @Transactional
    public void updateSequenceOfStopsFromDb(Shaft shaft) {
        long shaftId = shaft.getId();
        ShaftDTO shaftDto;

        try {
            shaftDto = shaftRepository.getReferenceById(shaftId);

            SequenceOfStops sequenceFromDb = JsonUtil.stringJSONToSequenceOfStops(shaftDto.getSequenceOfStops());

            if (sequenceFromDb != null) {
                sequenceFromDb.addListener(this);
            }

            shaft.setSequenceOfStops(sequenceFromDb);
        } catch (EntityNotFoundException e) {
            log.error("shaft Id {} not found in DB", shaftId);
            throw new IllegalStateException("shaft Id " + shaftId + " not found in DB");
        }
    }

    @Override
    public void handleCabinPositionChanged(CabinPositionChangedEvent event) {
        Shaft shaft = event.getShaft();
        shaftRepository.updateCabinPositionById(shaft.getId(), event.getShaft().getCabinPosition());
        log.debug("Shaft with id [{}] updated cabinPosition in DB to: [{}]", shaft.getId(), event.getShaft().getCabinPosition());
    }

    @Override
    public void handleSequenceOfStopsChanged(SequenceOfStopsChangedEvent event) {
        Shaft shaft = event.getShaft();
        shaftRepository.updateSequenceById(shaft.getId(), JsonUtil.sequenceToStringInJSON(shaft.getSequenceOfStops()));
        log.debug("Shaft with id [{}] updated sequenceOfStops in DB to: [{}]", shaft.getId(), shaft.getSequenceOfStops());
    }
}