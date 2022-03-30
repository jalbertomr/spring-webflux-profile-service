# Spring WebFlux profile service
#### create Profile Model
- create Profile Model

   String id
   
   String email
   
#### Create ProfileRepository

public interface ProfileRepository extends ReactiveMongoRepository<Profile, String> {
}   

#### Initialize the Repository when ApplicationReadyEvent by ApplicationListener, Fill and Show the repository

![image of initializa repo when appready](https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEiTHteJlSTx0e8ZVQoBUU3iLQdLtn5Q36zMg_PisYfJXAace4L95yzm9xJBrGBAK06UPVWE_qISH0PS2dlN9wwB3Pxav6JGPHi46jntvPEdjumeUzWe6sbLIIE6Bj8xgRWErMVhznyO-T6CQBWKgcFx0EMSS21MudRlEaZuJ3JnnSXUiIooJ8IjInz5/s894/InitializeOnAppReady.JPG)


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
                .subscribe(log::info);
    }


> vm options to run faster : -noverify -XX:TieredStopAtLevel=1
#### Using new Subscribe()
      ...

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
            
#### Using .subscribe( new Consumer ...

      this.profileRepository.deleteAll()
            .thenMany( profiles)
            .thenMany( this.profileRepository.findAll())
            .subscribe(new Consumer<Profile>() {
                @Override
                public void accept(Profile profile) {
                    log.info("Consumer.accept: {}" , profile);
                }
            });            
            
#### Sample of IO Synchronous and Asynchronous Read, from an input.txt file wich has been located 
  on user Desktop folder
  
#### Profile Service

-   all()
-   byId(String id)              
- create( Profile))

#### Event Sourcing
###### When a new record is created an event is shoted to be comunicated with other components in the same JVM, like on CQRS

An ApplicationEvent with the profile is needed

    public class ProfileCreatedEvent extends ApplicationEvent {
       public ProfileCreatedEvent(Profile source) {
           super(source);
       }
    }

And is activated when the profile is created

    service
    ...
    private final ApplicationEventPublisher applicationEventPublisher;
    ...
    Mono<Profile> create(String email){
        return this.profileRepository.save( new Profile(null, email))
                .doOnSuccess(profile -> this.applicationEventPublisher.publishEvent( new ProfileCreatedEvent( profile)));
    }

#### A Profile Controller 

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

## Test the controller
To test the profile controller is necessary disable the okta dependency, other way a login window in browser is shown.

