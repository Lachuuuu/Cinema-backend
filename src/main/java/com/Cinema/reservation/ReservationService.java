package com.Cinema.reservation;

import com.Cinema.reservation.dto.ReservationDto;
import com.Cinema.reservation.request.AddReservationRequest;
import com.Cinema.security.auth.exception.BadRequestException;
import com.Cinema.showing.Showing;
import com.Cinema.showing.ShowingRepository;
import com.Cinema.showing.ShowingService;
import com.Cinema.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
   private final ReservationRepository reservationRepository;

   @Value("${price.normal}")
   private BigDecimal normalTicketPrice;

   @Value("${price.discount}")
   private BigDecimal discountTicketPrice;

   private final ShowingRepository showingRepository;

   private final ShowingService showingService;

   private final ReservationAssembler reservationAssembler;

   public Reservation add(User user, AddReservationRequest addReservationRequest) throws BadRequestException {

      Showing showing = showingRepository.findById(addReservationRequest.getShowingId()).orElse(null);
      if (showing == null) throw new BadRequestException("showing do not exist");

      if (addReservationRequest.getNormal() + addReservationRequest.getDiscount() != addReservationRequest.getSeatIds().size())
         throw new BadRequestException("wrong number of choosen seats");

      if (addReservationRequest.getDiscount() + addReservationRequest.getNormal() < 1) return null;
      Reservation newReservation = new Reservation(null,
            user,
            addReservationRequest.getSeatIds(),
            addReservationRequest.getNormal(),
            addReservationRequest.getDiscount(),
            calculateTotalValue(addReservationRequest.getNormal(), addReservationRequest.getDiscount()),
            showing
      );

      showingService.updateSeatsMap(showing, addReservationRequest.getSeatIds());

      return reservationRepository.save(newReservation);
   }

   public Set<ReservationDto> getUserReservations(User user) {
      List<Reservation> userReservations = reservationRepository.findAllByUserOrderById(user);
      return userReservations.stream()
            .map(reservationAssembler::toDto)
            .collect(Collectors.toSet());
   }

   public void remove(User user, Long reservationId) throws BadRequestException {
      Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new BadRequestException("Reservation not found"));
      if (!reservation.getUser().equals(user)) throw new BadRequestException("You are not owner of the reservation");

      reservationRepository.delete(reservation);
   }

   private BigDecimal calculateTotalValue(Long normal, Long discount) {
      return normalTicketPrice.multiply(BigDecimal.valueOf(normal)).add(discountTicketPrice.multiply(BigDecimal.valueOf(discount)));
   }

}
