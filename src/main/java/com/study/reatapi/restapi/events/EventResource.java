package com.study.reatapi.restapi.events;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class EventResource extends Resource<Event> {

    public EventResource(Event event, Link... links) {
        super(event, links);
        ControllerLinkBuilder linkBuilder = linkTo(EventController.class).slash(event.getId());
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
        add(linkTo(EventController.class).withRel("query-events"));
        add(linkBuilder.withRel("update-event"));
        add(new Link("/docs/index.html#resources-events-create").withRel("profile"));
    }
}
