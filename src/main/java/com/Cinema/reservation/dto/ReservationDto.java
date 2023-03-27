package com.Cinema.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
   private Long id;

   private List<Long> seatIds;

   private Long normal;

   private Long discount;

   private BigDecimal totalValue;

   private Long hallId;

   private LocalDateTime showingTime;

   private String seatsMap;

   private String movieTitle;
}
