package com.Cinema.movie.request;

import java.time.LocalDate;
import java.util.Set;

public class NewMovieRequest {
   private String name;

   private String description;

   private Long durationMin;

   private LocalDate premiereDate;

   private Set<Long> genres;

   private Long minAge;

   private String image;

   public NewMovieRequest() {
   }

   public NewMovieRequest(String name, String description, Long durationMin, LocalDate premiereDate, Set<Long> genres, Long minAge, String image) {
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

   public LocalDate getPremiereDate() {
      return premiereDate;
   }

   public void setPremiereDate(LocalDate premiereDate) {
      this.premiereDate = premiereDate;
   }

   public Set<Long> getGenres() {
      return genres;
   }

   public void setGenres(Set<Long> genres) {
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
