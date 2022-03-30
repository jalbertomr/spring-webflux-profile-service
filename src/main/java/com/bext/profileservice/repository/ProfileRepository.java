package com.bext.profileservice.repository;


import com.bext.profileservice.model.Profile;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProfileRepository extends ReactiveMongoRepository<Profile, String> {
}
