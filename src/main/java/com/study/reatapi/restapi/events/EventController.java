package com.study.reatapi.restapi.events;

import com.study.reatapi.restapi.common.ErrorResource;
import com.study.reatapi.restapi.events.dto.EventSaveRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventRepository eventRepository;
    private final EventService eventService;

    @PostMapping
    public ResponseEntity createEvents(@RequestBody @Valid EventSaveRequestDto eventDto, Errors errors) {
        Event event = eventDto.dtoToEntity();
        if(errors.hasErrors()) return getBadRequest(errors);
        Errors wrongEventValue = event.wasWrongValue(errors);
        if(wrongEventValue.hasErrors()) return getBadRequest(wrongEventValue);

        Event savedEvent = eventService.createEvent(event);;
        URI uri = linkTo(EventController.class).slash(savedEvent.getId()).toUri();

        return ResponseEntity.created(uri).body(new EventResource(savedEvent));
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
        Page<Event> events = eventService.getEvents(pageable);
        var resource = assembler.toResource(events, e -> new EventResource(e));
        return ResponseEntity.ok().body(resource);
    }

    private ResponseEntity getBadRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorResource(errors));
    }
}
