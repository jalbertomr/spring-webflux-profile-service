package com.bext.profileservice.glue;

import com.bext.profileservice.event.ProfileCreatedEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

@Component
public class ProfileCreatedEventPublisher implements ApplicationListener<ProfileCreatedEvent>, Consumer<FluxSink<ProfileCreatedEvent>> {

    private final BlockingDeque<ProfileCreatedEvent> profileEventsDeque = new LinkedBlockingDeque<>();
    private final Executor executor;

    public ProfileCreatedEventPublisher( Executor executor) {
        this.executor = executor;
    }

    @Override
    public void accept(FluxSink<ProfileCreatedEvent> profileCreatedEventFluxSink) {
        this.executor.execute(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        ProfileCreatedEvent profileCreatedEvent = profileEventsDeque.take();
                        profileCreatedEventFluxSink.next(profileCreatedEvent);
                    } catch (InterruptedException e) {
                        ReflectionUtils.rethrowRuntimeException(e);
                    }
                }
            }
        });
    }

    @Override
    public void onApplicationEvent(ProfileCreatedEvent profileCreatedEvent) {
        this.profileEventsDeque.offer(profileCreatedEvent);
    }
}
