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

@RestController
@AllArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
   private final UserService userService;

   private final ReservationService reservationService;

   private final Gson gson;

   @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> addReservation(
         @CookieValue(name = "jwt") String token,
         @RequestBody AddReservationRequest addReservationRequest
   ) throws BadRequestException {
      User user = userService.getUser(token);
      reservationService.addReservation(user, addReservationRequest);
      return ResponseEntity.ok(gson.toJson("Successfully made reservation"));
   }

   @GetMapping(value = "/user")
   public ResponseEntity<Set<ReservationDto>> getUserReservations(
         @CookieValue(name = "jwt") String token
   ) throws BadRequestException {
      User user = userService.getUser(token);
      Set<ReservationDto> listOfUserReservations = reservationService.getUserReservations(user);
      return ResponseEntity.ok().body(listOfUserReservations);
   }

   @ExceptionHandler({BadRequestException.class})
   public ResponseEntity<String> handleInvalidTopTalentDataException(BadRequestException e) {
      return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
   }
}
