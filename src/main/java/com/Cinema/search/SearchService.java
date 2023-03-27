package com.Cinema.search;

import com.Cinema.movie.Movie;
import com.Cinema.movie.MovieAssembler;
import com.Cinema.movie.MovieRepository;
import com.Cinema.movie.dto.MovieDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchService {

   private final MovieRepository movieRepository;

   private final MovieAssembler movieAssembler;

   public Set<MovieDto> search(String query) {
      List<Movie> movies = movieRepository.findAll();
      List<Movie> moviesByName = movies.stream().filter(it -> it.getName().contains(query)).collect(Collectors.toList());
      List<Movie> moviesByGenre = movies.stream().filter(
            it -> !it.getGenres().stream().filter(genre -> genre.getName().contains(query)).findAny().isEmpty()
      ).collect(Collectors.toList());

      Set<Movie> mergedSet = new HashSet<>();
      mergedSet.addAll(moviesByName);
      mergedSet.addAll(moviesByGenre);

      Set<MovieDto> result = new HashSet<>();
      mergedSet.forEach(it -> result.add(movieAssembler.toMovieDto(it)));

      return result;
   }
}
