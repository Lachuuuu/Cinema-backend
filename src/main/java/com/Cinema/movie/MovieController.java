package com.Cinema.movie;

import com.Cinema.movie.dto.MovieDto;
import com.Cinema.movie.request.NewMovieRequest;
import com.Cinema.security.auth.exception.BadRequestException;
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

   @PostMapping(value = "/admin/add", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> addMovie(@RequestBody NewMovieRequest request) {
      Movie movie = movieService.addMovie(request);
      if (movie != null) return ResponseEntity.ok(gson.toJson("Movie added successfully"));
      else return ResponseEntity.badRequest().body(gson.toJson("Error occurred while trying to add movie"));
   }

   @GetMapping(value = "/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<MovieDto> getMovieById(@PathVariable Long movieId) {
      Movie movie = movieRepository.findById(movieId).orElse(null);

      if (movie != null) {
         MovieDto movieDto = movieAssembler.toMovieDto(movie);
         return ResponseEntity.ok(movieDto);
      } else
         return ResponseEntity.badRequest().body(null);
   }

   @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<MovieDto>> getAllMovies() {
      List<Movie> movies = movieRepository.findAll();
      List<MovieDto> result = movies.stream().map(it -> movieAssembler.toMovieDto(it)).collect(Collectors.toList());
      if (result != null) return ResponseEntity.ok(result);
      else
         return ResponseEntity.badRequest().body(null);
   }

   @DeleteMapping(value = "/admin/remove/{movieId}")
   public ResponseEntity removeMovie(@PathVariable Long movieId) throws BadRequestException {
      movieService.removeMovie(movieId);
      return ResponseEntity.ok(null);
   }

   @ExceptionHandler({BadRequestException.class})
   public ResponseEntity<String> handleInvalidTopTalentDataException(BadRequestException e) {
      return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
   }

}

