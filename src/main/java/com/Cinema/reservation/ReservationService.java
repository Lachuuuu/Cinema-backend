package com.Cinema.reservation;

import com.Cinema.reservation.request.AddReservationRequest;
import com.Cinema.security.auth.exception.BadRequestException;
import com.Cinema.showing.Showing;
import com.Cinema.showing.ShowingRepository;
import com.Cinema.showing.ShowingService;
import com.Cinema.user.User;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
   private final ReservationRepository reservationRepository;

   @Value("${price.normal}")
   private BigDecimal normalTicketPrice;

   @Value("${price.discount}")
   private BigDecimal discountTicketPrice;

   private final Validator validator;

   private final ShowingRepository showingRepository;

   private final ShowingService showingService;

   public Reservation addReservation(User user, AddReservationRequest addReservationRequest) throws BadRequestException {

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

      final Set<ConstraintViolation<Reservation>> constraints = validator.validate(newReservation);
      if (!constraints.isEmpty()) throw new BadRequestException(constraints.iterator().next().getMessage());

      try {
         showingService.updateSeatsMap(showing, addReservationRequest.getSeatIds());
      } catch (BadRequestException e) {
         throw new BadRequestException(e.getMessage());
      }

      reservationRepository.save(newReservation);
      return newReservation;
   }

   public Set<Reservation> getUserReservations(User user) {
      return reservationRepository.findAllByUser(user);
   }

   private BigDecimal calculateTotalValue(Long normal, Long discount) {
      return normalTicketPrice.multiply(BigDecimal.valueOf(normal)).add(discountTicketPrice.multiply(BigDecimal.valueOf(discount)));
   }

}
