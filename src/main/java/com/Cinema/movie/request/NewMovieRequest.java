package com.Cinema.movie.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
   @NotBlank(message = "name cannot be blank")
   private String name;

   @NotBlank(message = "description cannot be blank")
   private String description;

   @Min(value = 1, message = "duration should be greater than 0")
   private Long durationMin;

   private LocalDate premiereDate;

   private Set<Long> genres;

   private Long minAge;

   private String image;
}
