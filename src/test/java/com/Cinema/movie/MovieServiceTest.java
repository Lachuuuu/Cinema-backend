package com.Cinema.movie;

import com.Cinema.movie.dto.MovieDto;
import com.Cinema.movie.genre.Genre;
import com.Cinema.movie.genre.GenreRepository;
import com.Cinema.movie.request.NewMovieRequest;
import com.Cinema.security.auth.exception.BadRequestException;
import com.Cinema.showing.Showing;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class MovieServiceTest {

   @Mock
   MovieRepository movieRepository;
   @InjectMocks
   @Spy
   MovieService movieService;

   @Mock
   MovieAssembler movieAssembler;

   @Mock
   GenreRepository genreRepository;

   @Test
   void Should_return_empty_list_when_there_are_no_movies_in_database() {
      // given
      List<Movie> moviesInDatabase = List.of();
      // when
      when(movieRepository.findAllByOrderById()).thenReturn(moviesInDatabase);
      // then
      assertEquals(List.of(), movieService.findAll());
   }

   @Test
   void Should_return_list_of_movieDtos_when_there_are_movies_in_database() {
      // given
      List<Movie> moviesInDatabase = List.of(
            new Movie(1L, "title", "des", 60L, LocalDate.now(), null, 10L, null, Set.of()),
            new Movie(2L, "title", "des", 60L, LocalDate.now(), null, 10L, null, Set.of())
      );
      List<MovieDto> moviesDtos = moviesInDatabase.stream().map(it -> movieAssembler.toDto(it)).collect(Collectors.toList());
      // when
      when(movieRepository.findAllByOrderById()).thenReturn(moviesInDatabase);
      // then
      assertEquals(moviesDtos, movieService.findAll());
   }

   @Test
   void Should_return_exception_when_movie_not_found() {
      // given
      Long movieId = 0L;
      // when
      when(movieRepository.findById(any())).thenReturn(Optional.empty());
      // then
      BadRequestException exception = assertThrows(BadRequestException.class, () -> movieService.find(movieId));
      assertEquals("Movie not found", exception.getMessage());
   }

   @Test
   void Should_return_movieDto_when_movie_found() throws BadRequestException {
      // given
      Long movieId = 0L;
      Movie movie = new Movie(0L,
            "title",
            "des",
            60L,
            LocalDate.now(),
            null,
            10L,
            null,
            Set.of()
      );
      // when
      when(movieRepository.findById(any())).thenReturn(Optional.of(movie));
      // then
      assertEquals(movieAssembler.toDto(movie), movieService.find(movieId));
   }

   @Test
   void Should_return_exception_when_movie_active_showings_not_empty() {
      // given
      Long movieId = 0L;
      Movie movie = new Movie(0L,
            "title",
            "des",
            60L,
            LocalDate.now(),
            null,
            10L,
            null,
            Set.of(new Showing(null,
                  null,
                  null,
                  null,
                  null,
                  null,
                  true,
                  null)
            )
      );
      // when
      when(movieRepository.findById(any())).thenReturn(Optional.of(movie));
      // then
      BadRequestException exception = assertThrows(BadRequestException.class, () -> movieService.remove(movieId));
      assertEquals("Showings uses this movie, delete them first", exception.getMessage());
   }

   @Test
   void Should_delete_movie_when_there_are_no_active_showings() throws BadRequestException {
      // given
      Long movieId = 0L;
      Movie movie = new Movie(0L,
            "title",
            "des",
            60L,
            LocalDate.now(),
            null,
            10L,
            null,
            Set.of(new Showing(null,
                  null,
                  null,
                  null,
                  null,
                  null,
                  false,
                  null)
            )
      );
      // when
      when(movieRepository.findById(any())).thenReturn(Optional.of(movie));
      // then
      assertEquals(ArrayList.class, movieService.remove(movieId).getClass());
      verify(movieRepository).delete(movie);
   }

   @Test
   void Should_add_new_movie() throws BadRequestException {
      // given
      NewMovieRequest newMovieRequest = new NewMovieRequest(
            "testowy",
            "desc",
            90L,
            LocalDate.now(),
            Set.of(0L, 1L),
            5L,
            null
      );

      Movie movie = new Movie(0L,
            "testowy",
            "desc",
            90L,
            newMovieRequest.getPremiereDate(),
            Set.of(
                  new Genre(0L, "komedia"),
                  new Genre(1L, "dramat")
            ),
            5L,
            "".getBytes(),
            Set.of()
      );
      // when
      when(genreRepository.findAllByIdIsIn(any()))
            .thenReturn(
                  Set.of(
                        new Genre(0L, "komedia"),
                        new Genre(1L, "dramat")
                  )
            );
      when(movieRepository.save(any())).thenReturn(movie);
      // then
      assertEquals(movie, movieService.add(newMovieRequest));
   }
}