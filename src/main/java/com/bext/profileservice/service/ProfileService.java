package com.bext.profileservice.service;

import com.bext.profileservice.event.ProfileCreatedEvent;
import com.bext.profileservice.model.Profile;
import com.bext.profileservice.repository.ProfileRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ProfileService(ProfileRepository profileRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.profileRepository = profileRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    Mono<Profile> create(String email){
        return this.profileRepository.save( new Profile(null, email))
                .doOnSuccess(profile -> this.applicationEventPublisher.publishEvent( new ProfileCreatedEvent( profile)));
    }

    Flux<Profile> all() {
        return this.profileRepository.findAll();
    }

    Mono<Profile> byId(String id){
        return this.profileRepository.findById(id);
    }
}
