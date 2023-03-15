package com.Cinema.movie;

import com.Cinema.movie.genre.Genre;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movie")
public class Movie {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @Column(name = "name")
   @NotBlank
   private String name;

   @Column(name = "description")
   private String description;

   @Column(name = "durationMin")
   private Long durationMin;

   @Column(name = "premiereDate")
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private LocalDate premiereDate;

   @Column(name = "genre")
   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(name = "movie_genres",
         joinColumns = @JoinColumn(name = "movie_id"),
         inverseJoinColumns = @JoinColumn(name = "genre_id"))
   private Set<Genre> genres = new HashSet<>();

   @Column(name = "minAge")
   private Long minAge;

   @Lob
   @Column(name = "image")
   private byte[] image;

   public Movie() {
   }

   public Movie(Long id, String name, String description, Long durationMin, LocalDate premiereDate, Set<Genre> genres, Long minAge, byte[] image) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.durationMin = durationMin;
      this.premiereDate = premiereDate;
      this.genres = genres;
      this.minAge = minAge;
      this.image = image;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
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

   public byte[] getImage() {
      return image;
   }

   public void setImage(byte[] image) {
      this.image = image;
   }
}
