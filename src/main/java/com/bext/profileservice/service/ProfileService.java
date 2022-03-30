package com.bext.profileservice.service;

import com.bext.profileservice.model.Profile;
import com.bext.profileservice.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    Mono<Profile> create(String email){
        return this.profileRepository.save( new Profile(null, email));
    }

    Flux<Profile> all() {
        return this.profileRepository.findAll();
    }

    Mono<Profile> byId(String id){
        return this.profileRepository.findById(id);
    }
}
