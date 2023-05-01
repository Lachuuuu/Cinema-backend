package com.Cinema.movie;

import com.Cinema.movie.dto.MovieDto;
import com.Cinema.movie.request.NewMovieRequest;
import com.Cinema.security.auth.exception.BadRequestException;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {

   private final MovieService movieService;
   private final Gson gson;

   @PostMapping(value = "/admin/add", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> add(@RequestBody @Valid NewMovieRequest request) throws BadRequestException {
      movieService.add(request);
      return ResponseEntity.ok(gson.toJson("Movie added successfully"));
   }

   @GetMapping(value = "/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<MovieDto> getById(@PathVariable Long movieId) throws BadRequestException {
      MovieDto movie = movieService.find(movieId);
      return ResponseEntity.ok(movie);
   }

   @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<MovieDto>> getAll() {
      List<MovieDto> movies = movieService.findAll();
      return ResponseEntity.ok(movies);
   }

   @DeleteMapping(value = "/admin/remove/{movieId}")
   public ResponseEntity<List<MovieDto>> remove(@PathVariable Long movieId) throws BadRequestException {
      List<MovieDto> movies = movieService.remove(movieId);
      return ResponseEntity.ok(movies);
   }

   @ExceptionHandler({BadRequestException.class})
   public ResponseEntity<String> exceptionsHandler(BadRequestException e) {
      return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
   }

   @ExceptionHandler({MethodArgumentNotValidException.class})
   public ResponseEntity<String> exceptionsHandlerForValidation(MethodArgumentNotValidException exception) {
      BindingResult result = exception.getBindingResult();
      List<FieldError> fieldErrors = result.getFieldErrors();
      return ResponseEntity.badRequest()
            .body(
                  gson.toJson(
                        fieldErrors.stream()
                              .map(DefaultMessageSourceResolvable::getDefaultMessage)
                              .collect(Collectors.joining(""))
                  )
            );
   }

}

