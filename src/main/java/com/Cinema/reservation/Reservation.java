package com.Cinema.reservation;

import com.Cinema.showing.Showing;
import com.Cinema.user.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservation")
@JsonIdentityInfo(
      generator = ObjectIdGenerators.PropertyGenerator.class,
      property = "id")
public class Reservation {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @ManyToOne
   @JoinColumn(name = "user_id")
   private User user;

   @ElementCollection
   private List<Long> seatIds;

   @Min(value = 0)
   private Long normal;

   @Min(value = 0)
   private Long discount;

   @Min(value = 0)
   private BigDecimal totalValue;

   @ManyToOne
   private Showing showing;
}
