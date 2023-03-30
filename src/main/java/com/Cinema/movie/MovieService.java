package com.Cinema.movie;

import com.Cinema.movie.dto.MovieDto;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieService {
   private final GenreRepository genreRepository;

   private final MovieRepository movieRepository;

   private final ShowingRepository showingRepository;

   private final MovieAssembler movieAssembler;

   public Movie addMovie(NewMovieRequest request) throws BadRequestException {
      Set<Genre> genres = genreRepository.findAllByIdIsIn(request.getGenres()).stream().collect(Collectors.toSet());

      try {
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
         return movieRepository.save(movie);
      } catch (Exception e) {
         throw new BadRequestException("Error occurred while trying to add movie");
      }
   }

   public List<MovieDto> removeMovie(Long movieId) throws BadRequestException {
      Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new BadRequestException("Movie not found"));
      Set<Showing> showings = showingRepository.findAll().stream()
            .filter(it -> (it.getMovie().equals(movie) && it.getIsActive()))
            .collect(Collectors.toSet());

      if (showings.isEmpty()) movieRepository.delete(movie);
      else throw new BadRequestException("Showings uses this movie, delete them first");

      List<Movie> movies = movieRepository.findAllByOrderById();
      List<MovieDto> result = movies.stream().map(it -> movieAssembler.toMovieDto(it)).collect(Collectors.toList());

      return result;

   }

   public MovieDto findMovie(Long movieId) throws BadRequestException {
      Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new BadRequestException("Movie not found"));
      return movieAssembler.toMovieDto(movie);
   }

   public List<MovieDto> findAllMovies() {
      List<Movie> movies = movieRepository.findAllByOrderById();
      List<MovieDto> result = movies.stream().map(it -> movieAssembler.toMovieDto(it)).collect(Collectors.toList());
      return result;
   }

}
