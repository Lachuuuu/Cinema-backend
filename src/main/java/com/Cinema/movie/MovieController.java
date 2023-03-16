package com.Cinema.movie;

import com.Cinema.movie.dto.MovieDto;
import com.Cinema.movie.request.NewMovieRequest;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/movie")
public class MovieController {

   private final MovieService movieService;
   private final Gson gson;
   private final MovieRepository movieRepository;
   private final MovieAssembler movieAssembler;

   @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> addMovie(@RequestBody NewMovieRequest request) {
      Movie movie = movieService.addMovie(request);
      if (movie != null) return ResponseEntity.ok(gson.toJson("Movie added successfully"));
      else return ResponseEntity.badRequest().body(gson.toJson("Error occurred while trying to add movie"));
   }

   @GetMapping(value = "/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> getMovieById(@PathVariable Long movieId) {
      Movie movie = movieRepository.findById(movieId).orElse(null);
      if (movie != null) return ResponseEntity.ok(gson.toJson(movieAssembler.toMovieDto(movie)));
      else return ResponseEntity.badRequest().body(gson.toJson("Wrong movie id"));
   }

   @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> getAllMovies() {
      List<Movie> movies = movieRepository.findAll();
      List<MovieDto> result = movies.stream().map(it -> movieAssembler.toMovieDto(it)).collect(Collectors.toList());
      if (result != null) return ResponseEntity.ok(gson.toJson(result));
      else return ResponseEntity.badRequest().body(gson.toJson("Cannot get all movies"));
   }

}
