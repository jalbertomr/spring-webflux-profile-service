package com.bext.profileservice.controller;

import com.bext.profileservice.model.Profile;
import com.bext.profileservice.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    Flux<Profile> all(){
        return this.profileService.all();
    }

    @RequestMapping("/{id}")
    Mono<Profile> byId(@PathVariable("id") String id){
        return this.profileService.byId(id);
    }

    @PostMapping
    Mono<ResponseEntity<Object>> create(@RequestBody Profile profile) {
        return this.profileService.create( profile.getEmail())
                .map(profileSaved -> ResponseEntity.created(URI.create("/profiles/" + profileSaved.getId()))
                        .build());

    }
}
