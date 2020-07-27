package com.study.reatapi.restapi.events;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventRepository eventRepository;

    @PostMapping
    public ResponseEntity createEvents(@RequestBody @Valid EventSaveRequestDto eventDto, Errors errors) {
        Event event = eventDto.dtoToEntity();
        if(errors.hasErrors()) return ResponseEntity.badRequest().build();
        if(event.wasWrongPrice() || event.wasWrongEndEventDate() ||
                event.wasWrongBeginEventDate() || event.wasWrongCloseEnrollmentEventDate())
            return ResponseEntity.badRequest().build();

        Event savedEvent = eventRepository.save(event);
        URI uri = linkTo(EventController.class).slash(savedEvent.getId()).toUri();
        return ResponseEntity.created(uri).body(savedEvent);
    }
}
