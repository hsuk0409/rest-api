package com.study.reatapi.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void 이벤트_정상적으로_생성() throws Exception {
        EventSaveRequestDto event = EventSaveRequestDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 07, 27, 12, 26))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 07, 28, 12, 00))
                .beginEventDateTime(LocalDateTime.of(2020, 07, 30, 12, 26))
                .endEventDateTime(LocalDateTime.of(2020, 07, 31, 12, 26))
                .basePrice(1000)
                .maxPrice(2000)
                .limitOfEnrollment(100)
                .location("판교 삼환 하이펙스")
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
        ;
    }

    @Test
    public void 이벤트_생성시_불필요한_입력값_있을경우() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 07, 27, 12, 26))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 07, 28, 12, 00))
                .beginEventDateTime(LocalDateTime.of(2020, 07, 30, 12, 26))
                .endEventDateTime(LocalDateTime.of(2020, 07, 31, 12, 26))
                .basePrice(1000)
                .maxPrice(2000)
                .limitOfEnrollment(100)
                .location("판교 삼환 하이펙스")
                .free(true)
                .offline(false)
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 이벤트_생성시_입력값_비어있을경우() throws Exception {
        EventSaveRequestDto eventDto = EventSaveRequestDto.builder().build();

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 이벤트_생성시_입력값_이상할경우() throws Exception {
        EventSaveRequestDto eventDto = EventSaveRequestDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 07, 31, 12, 26))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 07, 30, 12, 00))
                .beginEventDateTime(LocalDateTime.of(2020, 07, 29, 12, 26))
                .endEventDateTime(LocalDateTime.of(2020, 07, 28, 12, 26))
                .basePrice(4000)
                .maxPrice(2000)
                .limitOfEnrollment(100)
                .location("판교 삼환 하이펙스")
                .build();

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}