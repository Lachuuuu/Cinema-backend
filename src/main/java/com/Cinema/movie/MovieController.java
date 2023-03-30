package com.Cinema.movie;

import com.Cinema.movie.dto.MovieDto;
import com.Cinema.movie.request.NewMovieRequest;
import com.Cinema.security.auth.exception.BadRequestException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {

   private final MovieService movieService;
   private final Gson gson;

   @PostMapping(value = "/admin/add", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> addMovie(@RequestBody NewMovieRequest request) throws BadRequestException {
      movieService.addMovie(request);
      return ResponseEntity.ok(gson.toJson("Movie added successfully"));
   }

   @GetMapping(value = "/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<MovieDto> getMovieById(@PathVariable Long movieId) throws BadRequestException {
      MovieDto movie = movieService.findMovie(movieId);
      return ResponseEntity.ok(movie);
   }

   @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<MovieDto>> getAllMovies() {
      List<MovieDto> movies = movieService.findAllMovies();
      return ResponseEntity.ok(movies);
   }

   @DeleteMapping(value = "/admin/remove/{movieId}")
   public ResponseEntity removeMovie(@PathVariable Long movieId) throws BadRequestException {
      List<MovieDto> movies = movieService.removeMovie(movieId);
      return ResponseEntity.ok(movies);
   }

   @ExceptionHandler({BadRequestException.class})
   public ResponseEntity<String> handleInvalidTopTalentDataException(BadRequestException e) {
      return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
   }

}

