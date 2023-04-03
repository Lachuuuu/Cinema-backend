package com.Cinema.search;

import com.Cinema.movie.dto.MovieDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

   private final SearchService searchService;

   @GetMapping(value = "/{search_query}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<Set<MovieDto>> getMovieById(@PathVariable String search_query) {
      Set<MovieDto> movies = searchService.search(search_query);
      return ResponseEntity.ok(movies);
   }
}
