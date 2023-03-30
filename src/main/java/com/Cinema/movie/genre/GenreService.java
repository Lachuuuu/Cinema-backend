package com.Cinema.movie.genre;

import com.Cinema.security.auth.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GenreService {

   private final GenreRepository genreRepository;

   public Set<Genre> getAllGenres() {
      return genreRepository.findAll().stream().collect(Collectors.toSet());
   }

   public Genre addGenre(String genreName) throws BadRequestException {
      if (!genreRepository.existsByName(genreName)) {
         Genre newGenre = new Genre(null, genreName);
         return genreRepository.save(newGenre);
      } else throw new BadRequestException("Genre with that name already exists");
   }

   public void removeGenre(Long genreId) throws BadRequestException {
      Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new BadRequestException("Genre not found"));
      genreRepository.delete(genre);
   }

}
