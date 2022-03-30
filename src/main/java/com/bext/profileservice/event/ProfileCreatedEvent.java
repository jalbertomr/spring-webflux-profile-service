package com.bext.profileservice.event;

import com.bext.profileservice.model.Profile;
import org.springframework.context.ApplicationEvent;

public class ProfileCreatedEvent extends ApplicationEvent {
    public ProfileCreatedEvent(Profile source) {
        super(source);
    }
}
