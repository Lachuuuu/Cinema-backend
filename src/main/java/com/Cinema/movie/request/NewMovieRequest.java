package com.Cinema.movie.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewMovieRequest {
   private String name;

   private String description;

   private Long durationMin;

   private LocalDate premiereDate;

   private Set<Long> genres;

   private Long minAge;

   private String image;
}
