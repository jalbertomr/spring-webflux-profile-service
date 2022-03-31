package com.bext.profileservice.controller;

import com.bext.profileservice.event.ProfileCreatedEvent;
import com.bext.profileservice.glue.ProfileCreatedEventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ServiceSendEventsController {

    private final ProfileCreatedEventPublisher profileCreatedEventPublisher;
    private final Flux<ProfileCreatedEvent> fluxProfileCreatedEvent;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    private String json(ProfileCreatedEvent pce){
        return this.objectMapper.writeValueAsString( pce);
    }

    public ServiceSendEventsController(ProfileCreatedEventPublisher profileCreatedEventPublisher, ObjectMapper objectMapper) {
        this.profileCreatedEventPublisher = profileCreatedEventPublisher;
        this.objectMapper = objectMapper;
        this.fluxProfileCreatedEvent = Flux.create( this.profileCreatedEventPublisher).share();
    }

    @GetMapping(produces= MediaType.TEXT_EVENT_STREAM_VALUE, value = "/sse/profiles")
    Flux<String> serviceSendEvent(){
        return this.fluxProfileCreatedEvent
                .map( profileCreatedEvent -> json(profileCreatedEvent));
    }
}
