package com.Cinema.reservation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddReservationRequest {

   private List<Long> seatIds;
   private Long normal;

   private Long discount;

   private Long showingId;
}
