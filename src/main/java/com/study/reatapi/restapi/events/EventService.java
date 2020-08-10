package com.study.reatapi.restapi.events;

import com.study.reatapi.restapi.events.dto.EventRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    @Transactional
    public Event createEvent(Event event) {
        event.verifyIsFreeForSetting();
        event.verifyIsOfflineForSetting();

        return eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public Page<Event> getEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Event getEvent(Integer id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Transactional
    public Event updateEvent(Integer id, EventRequestDto requestDto) {
        Event event = eventRepository.findById(id).orElse(null);
        event.verifyIsFreeForSetting();
        event.verifyIsOfflineForSetting();

        return event.update(requestDto);
    }
}
