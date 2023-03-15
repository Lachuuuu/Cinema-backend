package com.Cinema.movie;

import com.Cinema.movie.genre.GenreRepository;
import com.Cinema.movie.request.NewMovieRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@Transactional
public class MovieService {
   @Autowired
   GenreRepository genreRepository;

   @Autowired
   MovieRepository movieRepository;

   public Movie addMovie(NewMovieRequest request) {
      Movie movie = new Movie(
            null,
            request.getName(),
            request.getDescription(),
            request.getDurationMin(),
            request.getPremiereDate(),
            genreRepository.findAllByIdIsIn(request.getGenres()),
            request.getMinAge(),
            Base64.getDecoder().decode(request.getImage().getBytes())
      );
      movieRepository.save(movie);
      return movie;
   }

}
