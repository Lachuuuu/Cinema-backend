package com.Cinema.reservation;

import com.Cinema.reservation.dto.ReservationDto;
import com.Cinema.reservation.request.AddReservationRequest;
import com.Cinema.security.auth.exception.BadRequestException;
import com.Cinema.user.User;
import com.Cinema.user.UserService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
   private final UserService userService;

   private final ReservationService reservationService;

   private final Gson gson;

   private final ReservationAssembler reservationAssembler;

   @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> addReservation(
         @CookieValue(name = "jwt") String token,
         @RequestBody AddReservationRequest addReservationRequest
   ) throws BadRequestException {
      User user = null;
      try {
         user = userService.getUser(token);
      } catch (BadRequestException ignored) {
      }
      reservationService.addReservation(user, addReservationRequest);
      return ResponseEntity.ok(gson.toJson("Successfully made reservation"));
   }

   @GetMapping(value = "/user")
   public ResponseEntity<Set<ReservationDto>> getUserReservations(
         @CookieValue(name = "jwt") String token
   ) {
      User user = null;
      try {
         user = userService.getUser(token);
      } catch (BadRequestException ignored) {
      }
      Set<Reservation> listOfUserReservations = reservationService.getUserReservations(user);
      Set<ReservationDto> result = listOfUserReservations.stream()
            .map(it -> reservationAssembler.toReservationDto(it))
            .collect(Collectors.toSet());
      return ResponseEntity.ok().body(result);
   }

   @ExceptionHandler({BadRequestException.class})
   public ResponseEntity<String> handleInvalidTopTalentDataException(BadRequestException e) {
      return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
   }
}
