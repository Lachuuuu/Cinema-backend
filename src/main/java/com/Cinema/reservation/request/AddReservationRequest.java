package com.Cinema.reservation.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
   @Size(min = 1, message = "reservation should contains atleast 1 seat")
   private List<Long> seatIds;
   private Long normal;
   private Long discount;

   @NotNull(message = "showing id cannot be null")
   private Long showingId;
}
