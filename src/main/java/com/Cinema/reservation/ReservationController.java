package com.Cinema.reservation;

import com.Cinema.reservation.dto.ReservationDto;
import com.Cinema.reservation.request.AddReservationRequest;
import com.Cinema.security.auth.exception.BadRequestException;
import com.Cinema.user.User;
import com.Cinema.user.UserService;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
   private final UserService userService;

   private final ReservationService reservationService;

   private final Gson gson;

   @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> add(
         @CookieValue(name = "jwt") String token,
         @RequestBody @Valid AddReservationRequest addReservationRequest
   ) throws BadRequestException {
      User user = userService.getUserByToken(token);
      reservationService.add(user, addReservationRequest);
      return ResponseEntity.ok(gson.toJson("Successfully made reservation"));
   }

   @GetMapping(value = "/user")
   public ResponseEntity<Set<ReservationDto>> getUserReservations(
         @CookieValue(name = "jwt") String token
   ) throws BadRequestException {
      User user = userService.getUserByToken(token);
      Set<ReservationDto> listOfUserReservations = reservationService.getUserReservations(user);
      return ResponseEntity.ok().body(listOfUserReservations);
   }

   @DeleteMapping(value = "/{reservationId}")
   public ResponseEntity<Boolean> remove(
         @PathVariable Long reservationId,
         @CookieValue(name = "jwt") String token
   ) throws BadRequestException {
      User user = userService.getUserByToken(token);
      reservationService.remove(user, reservationId);
      return ResponseEntity.ok(true);
   }

   @ExceptionHandler({BadRequestException.class})
   public ResponseEntity<String> exceptionsHandler(BadRequestException e) {
      return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
   }

   @ExceptionHandler({MethodArgumentNotValidException.class})
   public ResponseEntity<String> exceptionsHandlerForValidation(MethodArgumentNotValidException exception) {
      BindingResult result = exception.getBindingResult();
      List<FieldError> fieldErrors = result.getFieldErrors();
      return ResponseEntity.badRequest()
            .body(
                  gson.toJson(
                        fieldErrors.stream()
                              .map(DefaultMessageSourceResolvable::getDefaultMessage)
                              .collect(Collectors.joining(""))
                  )
            );
   }
}
