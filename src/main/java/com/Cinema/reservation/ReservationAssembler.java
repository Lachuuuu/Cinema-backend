package com.Cinema.reservation;

import com.Cinema.reservation.dto.ReservationDto;
import org.springframework.stereotype.Service;

@Service
public class ReservationAssembler {

   public ReservationDto toDto(Reservation reservation) {
      return new ReservationDto(reservation.getId(),
            reservation.getSeatIds(),
            reservation.getNormal(),
            reservation.getDiscount(),
            reservation.getTotalValue(),
            reservation.getShowing().getHall().getId(),
            reservation.getShowing().getShowingStartTime(),
            reservation.getShowing().getSeatsMap(),
            reservation.getShowing().getMovie().getName()
      );
   }
}
