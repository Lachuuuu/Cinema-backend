package com.Cinema.movie.genre;

import com.Cinema.security.auth.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GenreService {

   private final GenreRepository genreRepository;

   public List<Genre> getAll() {
      return genreRepository.findAll();
   }

   public Genre add(String genreName) throws BadRequestException {
      if (!genreRepository.existsByName(genreName)) {
         Genre newGenre = new Genre(null, genreName);
         return genreRepository.save(newGenre);
      } else throw new BadRequestException("Genre with that name already exists");
   }

   public List<Genre> remove(Long genreId) throws BadRequestException {
      Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new BadRequestException("Genre not found"));
      genreRepository.delete(genre);

      return genreRepository.findAll();
   }

}
