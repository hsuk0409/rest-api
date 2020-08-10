package com.study.reatapi.restapi.events.dto;

import com.study.reatapi.restapi.events.Event;
import com.study.reatapi.restapi.events.EventStatus;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class EventRequestDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private LocalDateTime beginEnrollmentDateTime;
    @NotNull
    private LocalDateTime closeEnrollmentDateTime;
    @NotNull
    private LocalDateTime beginEventDateTime;
    @NotNull
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    @Min(0)
    private int basePrice; // (optional)
    @Min(0)
    private int maxPrice; // (optional)
    @Min(0)
    private int limitOfEnrollment;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    @Builder
    public EventRequestDto(String name, String description, LocalDateTime beginEnrollmentDateTime, LocalDateTime closeEnrollmentDateTime,
                           LocalDateTime beginEventDateTime, LocalDateTime endEventDateTime, String location, int basePrice, int maxPrice,
                           int limitOfEnrollment, EventStatus eventStatus) {
        this.name = name;
        this.description = description;
        this.beginEnrollmentDateTime = beginEnrollmentDateTime;
        this.closeEnrollmentDateTime = closeEnrollmentDateTime;
        this.beginEventDateTime = beginEventDateTime;
        this.endEventDateTime = endEventDateTime;
        this.location = location;
        this.basePrice = basePrice;
        this.maxPrice = maxPrice;
        this.limitOfEnrollment = limitOfEnrollment;
        this.eventStatus = eventStatus;
    }

    public Event dtoToEntity() {
        return Event.builder()
                .name(this.name)
                .description(this.description)
                .beginEnrollmentDateTime(this.beginEnrollmentDateTime)
                .closeEnrollmentDateTime(this.closeEnrollmentDateTime)
                .beginEventDateTime(this.beginEventDateTime)
                .endEventDateTime(this.endEventDateTime)
                .location(this.location)
                .basePrice(this.basePrice)
                .maxPrice(this.maxPrice)
                .limitOfEnrollment(this.limitOfEnrollment)
                .eventStatus(this.eventStatus)
                .build();
    }
}
