package com.Cinema.movie.genre;

import com.Cinema.security.auth.exception.BadRequestException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genre")
public class GenreController {

   private final GenreService genreService;

   private final Gson gson;

   @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<Set<Genre>> getAllMovies() {
      Set<Genre> genres = genreService.getAllGenres();
      return ResponseEntity.ok(genres);
   }

   @PostMapping(value = "/admin/add")
   public ResponseEntity addNewGenre(@RequestParam String name) throws BadRequestException {
      genreService.addGenre(name);
      return ResponseEntity.ok(null);
   }

   @DeleteMapping(value = "/admin/remove")
   public ResponseEntity removeGenre(@RequestParam Long genreId) throws BadRequestException {
      genreService.removeGenre(genreId);
      return ResponseEntity.ok(null);
   }

   @ExceptionHandler({BadRequestException.class})
   public ResponseEntity<String> handleInvalidTopTalentDataException(BadRequestException e) {
      return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
   }

}
