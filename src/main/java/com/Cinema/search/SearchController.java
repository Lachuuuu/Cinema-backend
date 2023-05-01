package com.Cinema.search;

import com.Cinema.movie.dto.MovieDto;
import com.Cinema.security.auth.exception.BadRequestException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

   private final SearchService searchService;

   private final Gson gson;

   @GetMapping(value = "/{search_query}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<Set<MovieDto>> searchByQuery(@PathVariable String search_query) {
      Set<MovieDto> movies = searchService.searchByQuery(search_query);
      return ResponseEntity.ok(movies);
   }

   @ExceptionHandler({BadRequestException.class})
   public ResponseEntity<String> exceptionsHandler(BadRequestException e) {
      return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
   }
}
