package com.Cinema.movie;

import com.Cinema.movie.genre.Genre;
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
}
