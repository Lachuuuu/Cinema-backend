package com.Cinema.movie.dto;

import com.Cinema.movie.genre.Genre;
import com.Cinema.showing.Showing;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {
   private Long id;

   private String name;

   private String description;

   private Long durationMin;

   private String premiereDate;

   private Set<Genre> genres;

   private Long minAge;

   private String image;

   private Set<Showing> showings;
}
