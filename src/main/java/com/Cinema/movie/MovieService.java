package com.Cinema.movie;

import com.Cinema.movie.genre.GenreRepository;
import com.Cinema.movie.request.NewMovieRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@AllArgsConstructor
@Transactional
public class MovieService {
   private final GenreRepository genreRepository;

   private final MovieRepository movieRepository;

   public Movie addMovie(NewMovieRequest request) {
      Movie movie = new Movie(
            null,
            request.getName(),
            request.getDescription(),
            request.getDurationMin(),
            request.getPremiereDate(),
            genreRepository.findAllByIdIsIn(request.getGenres()),
            request.getMinAge(),
            Base64.getDecoder().decode(request.getImage().getBytes()),
            null
      );
      movieRepository.save(movie);
      return movie;
   }

}
