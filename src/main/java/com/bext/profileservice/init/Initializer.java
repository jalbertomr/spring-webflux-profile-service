package com.bext.profileservice.init;

import com.bext.profileservice.model.Profile;
import com.bext.profileservice.repository.ProfileRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Log4j2
@Component
public class Initializer implements ApplicationListener<ApplicationReadyEvent> {

    private final ProfileRepository profileRepository;

    public Initializer(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
    Flux<Profile> profiles = Flux
            .just("Hugo","Paco","Luis","Daisy")
            .map( email -> new Profile(null, email))
            .flatMap( this.profileRepository::save);

        this.profileRepository.deleteAll()
                .thenMany( profiles)
                .thenMany( this.profileRepository.findAll())
                .subscribe(new Consumer<Profile>() {
                    @Override
                    public void accept(Profile profile) {
                        log.info("Consumer.accept: {}" , profile);
                    }
                });
    }
}
