# Spring WebFlux profile service
#### create Profile Model
- create Profile Model

   String id
   
   String email
   
- create ProfileRepository

public interface ProfileRepository extends ReactiveMongoRepository<Profile, String> {
}   

#### Initialize the Repository when ApplicationReadyEvent by ApplicationListener, Fill and Show the repository

![image of initializa repo when appready](https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEiTHteJlSTx0e8ZVQoBUU3iLQdLtn5Q36zMg_PisYfJXAace4L95yzm9xJBrGBAK06UPVWE_qISH0PS2dlN9wwB3Pxav6JGPHi46jntvPEdjumeUzWe6sbLIIE6Bj8xgRWErMVhznyO-T6CQBWKgcFx0EMSS21MudRlEaZuJ3JnnSXUiIooJ8IjInz5/s894/InitializeOnAppReady.JPG)
