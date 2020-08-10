package com.study.reatapi.restapi.events;

import com.study.reatapi.restapi.events.dto.EventRequestDto;
import lombok.*;
import org.springframework.validation.Errors;

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
                 boolean offline, boolean free, EventStatus eventStatus) {
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
        this.eventStatus = eventStatus;
    }

    public boolean wasWrongPrice() {
        return this.maxPrice > 0 && this.basePrice > this.maxPrice;
    }

    public boolean waWrongBeginEnrollmentDate() {
        return this.beginEnrollmentDateTime.isAfter(this.beginEventDateTime) ||
                this.beginEnrollmentDateTime.isAfter(this.closeEnrollmentDateTime) ||
                this.beginEnrollmentDateTime.isAfter(this.endEventDateTime);
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

    public void verifyIsFreeForSetting() {
        this.free =  this.basePrice == 0 && this.maxPrice == 0 ? true : false;
    }

    public void verifyIsOfflineForSetting() {
        this.offline = this.location == null ? false : true;
    }

    public Errors wasWrongValue(Errors errors) {
        if(wasWrongPrice()) errors.reject("wrongPrices", "Value of prices are wrong");
        if(waWrongBeginEnrollmentDate()) errors.rejectValue("beginEnrollmentDateTime", "wrongValue", "BeginEnrollmentDateTime of event is wrong");
        if(wasWrongBeginEventDate()) errors.rejectValue("beginEventDateTime", "wrongValue", "BeginEventDateTime of event is wrong");
        if(wasWrongEndEventDate()) errors.rejectValue("endEventDateTime", "wrongValue", "EndEventDateTime of event is wrong");
        if(wasWrongCloseEnrollmentEventDate()) errors.rejectValue("closeEnrollmentDateTime",
                "wrongValue", "CloseEnrollmentDateTime of event is wrong");
        return errors;
    }

    public Event update(EventRequestDto requestDto) {
        this.name = requestDto.getName();
        this.description = requestDto.getDescription();
        this.beginEnrollmentDateTime = requestDto.getBeginEnrollmentDateTime();
        this.closeEnrollmentDateTime = requestDto.getCloseEnrollmentDateTime();
        this.beginEventDateTime = requestDto.getBeginEventDateTime();
        this.endEventDateTime = requestDto.getEndEventDateTime();
        this.location = requestDto.getLocation();
        this.basePrice = requestDto.getBasePrice();
        this.maxPrice = requestDto.getMaxPrice();
        this.limitOfEnrollment = requestDto.getLimitOfEnrollment();
        return this;
    }

    public EventRequestDto toRequestDto() {
        return EventRequestDto.builder()
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
                .build();
    }
}
