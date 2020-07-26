package com.study.reatapi.restapi.events;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    @PostMapping
    public ResponseEntity createEvents(@RequestBody Event event) {
        URI uri = linkTo(EventController.class).slash("{id}").toUri();
        event.initEvent();
        return ResponseEntity.created(uri).body(event);
    }
}
