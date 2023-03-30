package com.Cinema.showing;

import com.Cinema.hall.Hall;
import com.Cinema.movie.Movie;
import com.Cinema.reservation.Reservation;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "showing")
@JsonIdentityInfo(
      generator = ObjectIdGenerators.PropertyGenerator.class,
      property = "id")
public class Showing {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;


   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "movie_id", nullable = false)
   private Movie movie;

   @ManyToOne
   @JoinColumn(name = "hall_id")
   private Hall hall;

   @OneToMany(mappedBy = "showing", cascade = CascadeType.REMOVE)
   private Set<Reservation> reservations;

   private LocalDateTime showingStartTime;

   private String seatsMap;

   private Boolean isActive;

   @NotBlank
   private String name;
}
