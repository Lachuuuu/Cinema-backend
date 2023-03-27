package com.Cinema.movie;

import com.Cinema.movie.genre.Genre;
import com.Cinema.showing.Showing;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
      generator = ObjectIdGenerators.PropertyGenerator.class,
      property = "id")
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

   @OneToMany(fetch = FetchType.EAGER, mappedBy = "movie")
   private Set<Showing> showings;
}
