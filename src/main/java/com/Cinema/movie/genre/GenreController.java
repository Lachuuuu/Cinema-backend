package com.Cinema.movie.genre;

import com.Cinema.security.auth.exception.BadRequestException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genre")
public class GenreController {

   private final GenreService genreService;

   private final Gson gson;

   @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<Genre>> getAll() {
      List<Genre> genres = genreService.getAll();
      return ResponseEntity.ok(genres);
   }

   @PostMapping(value = "/admin/add")
   public ResponseEntity<Genre> add(@RequestParam String name) throws BadRequestException {
      Genre genre = genreService.add(name);
      return ResponseEntity.ok(genre);
   }

   @DeleteMapping(value = "/admin/remove")
   public ResponseEntity<List<Genre>> remove(@RequestParam Long genreId) throws BadRequestException {
      List<Genre> genres = genreService.remove(genreId);
      return ResponseEntity.ok(genres);
   }

   @ExceptionHandler({BadRequestException.class})
   public ResponseEntity<String> exceptionsHandler(BadRequestException e) {
      return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
   }

}
