package com.study.reatapi.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.reatapi.restapi.common.RestDocsConfiguration;
import com.study.reatapi.restapi.events.dto.EventSaveRequestDto;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventRepository eventRepository;

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
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query-events"),
                                linkWithRel("update-event").description("link to update-event"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept headers"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("BeginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("CloseEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("BeginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("EndEventDateTime of new event"),
                                fieldWithPath("location").description("Location of new event"),
                                fieldWithPath("basePrice").description("BasePrice of new event"),
                                fieldWithPath("maxPrice").description("MaxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("LimitOfEnrollment of new event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("POST /api/events/"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("HAL JSON TYPE")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Id of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("BeginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("CloseEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("BeginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("EndEventDateTime of new event"),
                                fieldWithPath("location").description("Location of new event"),
                                fieldWithPath("basePrice").description("BasePrice of new event"),
                                fieldWithPath("maxPrice").description("MaxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("LimitOfEnrollment of new event"),
                                fieldWithPath("free").description("Free of new event"),
                                fieldWithPath("offline").description("Offline of new event"),
                                fieldWithPath("eventStatus").description("EventStatus of new event"),
                                fieldWithPath("_links.self.href").type(JsonFieldType.STRING).description("My href").optional(),
                                fieldWithPath("_links.query-events.href").type(JsonFieldType.STRING).description("Query-events href").optional(),
                                fieldWithPath("_links.update-event.href").type(JsonFieldType.STRING).description("Update-event href").optional(),
                                fieldWithPath("_links.profile.href").type(JsonFieldType.STRING).description("Profile href").optional()
                        )
                ));
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("content[0].objectName").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    public void 서른개의_이벤트_열개씩_두번째_페이지조회() throws Exception {
        //given
        IntStream.range(0, 30).forEach(this::generateEvent);

        //when
        mockMvc.perform(get("/api/events")
                    .param("pae", "1")
                    .param("size", "10")
                    .param("sort", "id,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.profile").exists())
                .andDo(document("query-events"))
        ;
    }

    private Event generateEvent(int i) {
        return eventRepository.save(Event.builder()
                .name("event" + i)
                .description("test event")
//                .beginEnrollmentDateTime(LocalDateTime.of(2020, 07, 31, 12, 26))
//                .closeEnrollmentDateTime(LocalDateTime.of(2020, 07, 30, 12, 00))
//                .beginEventDateTime(LocalDateTime.of(2020, 07, 29, 12, 26))
//                .endEventDateTime(LocalDateTime.of(2020, 07, 28, 12, 26))
//                .basePrice(4000)
//                .maxPrice(2000)
//                .limitOfEnrollment(100)
//                .location("판교 삼환 하이펙스")
                .build());
    }
}