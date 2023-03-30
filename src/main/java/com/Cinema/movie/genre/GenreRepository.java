package com.Cinema.movie.genre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

   Set<Genre> findAllByIdIsIn(Set<Long> genreIds);

   Boolean existsByName(String name);

}