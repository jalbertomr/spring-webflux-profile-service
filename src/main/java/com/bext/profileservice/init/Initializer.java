package com.bext.profileservice.init;

import com.bext.profileservice.model.Profile;
import com.bext.profileservice.repository.ProfileRepository;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

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
                .just("Hugo", "Paco", "Luis", "Daisy")
                .map(email -> new Profile(null, email))
                .flatMap(this.profileRepository::save);

        this.profileRepository.deleteAll()
                .thenMany(profiles)
                .thenMany(this.profileRepository.findAll())
                .subscribe(new Subscriber<Profile>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        log.info("onSubscribe: {}", s);
                        s.request( profiles.count().block());
                    }

                    @Override
                    public void onNext(Profile profile) {
                        log.info("onNext: {}", profile);
                    }

                    @Override
                    public void onError(Throwable t) {
                        log.error("onError");
                    }

                    @Override
                    public void onComplete() {
                        log.info("onComplete: Completed!");
                    }
                });
    }
}
