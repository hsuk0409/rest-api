package com.study.reatapi.restapi.events;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder().build();
        assertThat(event).isNotNull();
    }

    @Test
    public void 이벤트가_free일_경우() {
        // Given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();

        // When
        event.validIsFree();
        // Then
        assertThat(event.isFree()).isTrue();
    }

    @Test
    public void 이벤트가_free가_아닐경우() {
        // Given
        Event event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();

        // When
        event.validIsFree();
        // Then
        assertThat(event.isFree()).isFalse();

        // Given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();

        // When
        event.validIsFree();
        // Then
        assertThat(event.isFree()).isFalse();
    }

    @Test
    public void offline_인지아닌지() {
        // Given
        Event event = Event.builder()
                .location("판교 삼환 하이펙스")
                .build();

        // When
        event.validIsOffline();

        // Then
        assertThat(event.isOffline()).isTrue();

        // Given
        event = Event.builder()
                .build();

        // When
        event.validIsOffline();

        // Then
        assertThat(event.isOffline()).isFalse();
    }
}