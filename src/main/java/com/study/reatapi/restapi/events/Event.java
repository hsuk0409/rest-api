package com.study.reatapi.restapi.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Entity
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    @Builder
    public Event(Integer id, String name, String description, LocalDateTime beginEnrollmentDateTime, LocalDateTime closeEnrollmentDateTime,
                 LocalDateTime beginEventDateTime, LocalDateTime endEventDateTime, String location, int basePrice, int maxPrice, int limitOfEnrollment,
                 boolean offline, boolean free) {
        this.id = id;
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
        this.offline = offline;
        this.free = free;
    }

    public void initId() {
        this.id = 10;
    }


    public boolean wasWrongPrice() {
        return this.maxPrice > 0 && this.basePrice > this.maxPrice;
    }

    public boolean wasWrongEndEventDate() {
        return this.endEventDateTime.isBefore(this.beginEnrollmentDateTime) ||
                this.endEventDateTime.isBefore(this.beginEventDateTime) ||
                this.endEventDateTime.isBefore(this.closeEnrollmentDateTime);
    }

    public boolean wasWrongBeginEventDate() {
        return this.beginEventDateTime.isAfter(this.endEventDateTime) ||
                this.beginEventDateTime.isBefore(this.beginEnrollmentDateTime) ||
                this.beginEventDateTime.isBefore(this.closeEnrollmentDateTime);
    }

    public boolean wasWrongCloseEnrollmentEventDate() {
        return this.closeEnrollmentDateTime.isBefore(this.beginEnrollmentDateTime) ||
                this.closeEnrollmentDateTime.isAfter(this.beginEventDateTime) ||
                this.closeEnrollmentDateTime.isAfter(this.endEventDateTime);
    }
}
