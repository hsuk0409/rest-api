package com.study.reatapi.restapi.events;

import com.study.reatapi.restapi.common.ErrorResource;
import com.study.reatapi.restapi.events.dto.EventRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
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
    public ResponseEntity createEvents(@RequestBody @Valid EventRequestDto eventDto, Errors errors) {
        Event event = eventDto.dtoToEntity();
        if(errors.hasErrors()) return getBadRequest(errors);
        Errors wrongEventValue = event.wasWrongValue(errors);
        if(wrongEventValue.hasErrors()) return getBadRequest(wrongEventValue);

        Event savedEvent = eventService.createEvent(event);;
        URI uri = linkTo(EventController.class).slash(savedEvent.getId()).toUri();
        EventResource resource = new EventResource(savedEvent);
        resource.add(new Link("/docs/index.html#resources-events-create").withRel("profile"));

        return ResponseEntity.created(uri).body(resource);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
        Page<Event> events = eventService.getEvents(pageable);
        var resource = assembler.toResource(events, e -> new EventResource(e));
        resource.add(new Link("/docs/index.html#resources-events-list").withRel("profile"));

        return ResponseEntity.ok().body(resource);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id) {
        Event event = eventService.getEvent(id);
        if(event == null) return ResponseEntity.notFound().build();
        EventResource resource = new EventResource(event);
        resource.add(new Link("/docs/index.html#resources-events-get").withRel("profile"));

        return ResponseEntity.ok().body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id,
                                      @RequestBody @Valid EventRequestDto requestDto,
                                      Errors errors) {
        Event existedEvent = eventService.getEvent(id);
        Event requestEvent = requestDto.dtoToEntity();
        if(existedEvent == null) return ResponseEntity.notFound().build();
        if(errors.hasErrors()) return ResponseEntity.badRequest().build();
        if(requestEvent.wasWrongValue(errors).hasErrors()) return ResponseEntity.badRequest().build();

        Event event = eventService.updateEvent(id, requestDto);
        EventResource resource = new EventResource(event);
        resource.add(new Link("/docs/index.html#resources-events-update").withRel("profile"));

        return ResponseEntity.ok().body(resource);
    }

    private ResponseEntity getBadRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorResource(errors));
    }
}
