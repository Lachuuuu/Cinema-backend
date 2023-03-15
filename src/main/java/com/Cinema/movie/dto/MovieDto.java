package com.Cinema.movie.dto;

import com.Cinema.movie.genre.Genre;

import java.util.Set;

public class MovieDto {
   private String name;

   private String description;

   private Long durationMin;

   private String premiereDate;

   private Set<Genre> genres;

   private Long minAge;

   private String image;

   public MovieDto() {
   }

   public MovieDto(String name, String description, Long durationMin, String premiereDate, Set<Genre> genres, Long minAge, String image) {
      this.name = name;
      this.description = description;
      this.durationMin = durationMin;
      this.premiereDate = premiereDate;
      this.genres = genres;
      this.minAge = minAge;
      this.image = image;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public Long getDurationMin() {
      return durationMin;
   }

   public void setDurationMin(Long durationMin) {
      this.durationMin = durationMin;
   }

   public String getPremiereDate() {
      return premiereDate;
   }

   public void setPremiereDate(String premiereDate) {
      this.premiereDate = premiereDate;
   }

   public Set<Genre> getGenres() {
      return genres;
   }

   public void setGenres(Set<Genre> genres) {
      this.genres = genres;
   }

   public Long getMinAge() {
      return minAge;
   }

   public void setMinAge(Long minAge) {
      this.minAge = minAge;
   }

   public String getImage() {
      return image;
   }

   public void setImage(String image) {
      this.image = image;
   }
}
