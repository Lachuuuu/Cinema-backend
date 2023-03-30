package com.Cinema.movie;

import com.Cinema.movie.genre.Genre;
import com.Cinema.movie.genre.GenreRepository;
import com.Cinema.movie.request.NewMovieRequest;
import com.Cinema.security.auth.exception.BadRequestException;
import com.Cinema.showing.Showing;
import com.Cinema.showing.ShowingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieService {
   private final GenreRepository genreRepository;

   private final MovieRepository movieRepository;

   private final ShowingRepository showingRepository;

   public Movie addMovie(NewMovieRequest request) {
      Set<Genre> genres = genreRepository.findAllByIdIsIn(request.getGenres()).stream().collect(Collectors.toSet());

      Movie movie = new Movie(
            null,
            request.getName(),
            request.getDescription(),
            request.getDurationMin(),
            request.getPremiereDate(),
            genres,
            request.getMinAge(),
            Base64.getDecoder().decode(request.getImage().getBytes()),
            null
      );
      movieRepository.save(movie);
      return movie;
   }

   public void removeMovie(Long movieId) throws BadRequestException {
      Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new BadRequestException("Movie not found"));
      Set<Showing> showings = showingRepository.findAll().stream()
            .filter(it -> (it.getMovie().equals(movie) && it.getIsActive()))
            .collect(Collectors.toSet());

      if (showings.isEmpty()) movieRepository.delete(movie);
      else throw new BadRequestException("Showings uses this movie, delete them first");

   }

}
