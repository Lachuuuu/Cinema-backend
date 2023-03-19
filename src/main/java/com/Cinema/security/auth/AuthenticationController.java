package com.Cinema.security.auth;

import com.Cinema.security.auth.exception.BadRequestException;
import com.Cinema.security.auth.request.AuthenticationRequest;
import com.Cinema.security.auth.request.RegisterRequest;
import com.Cinema.security.auth.verification.EmailConfirmationService;
import com.Cinema.user.User;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

   private final AuthenticationService authenticationService;
   private final CookieService cookieService;
   private final EmailConfirmationService emailConfirmationService;
   private final Gson gson;

   @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> register(
         @RequestBody RegisterRequest request
   ) throws BadRequestException {
      final User user = authenticationService.register(request);
      if (user != null) emailConfirmationService.sendEmail(user);
      return ResponseEntity.ok(gson.toJson("Verify email by the link sent on your email address"));
   }

   @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> auth(
         @RequestBody AuthenticationRequest request,
         HttpServletResponse response
   ) {
      return cookieService.createAuthenticationCookies(request, response);
   }

   @GetMapping(value = "/confirm-account", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> confirmUserAccount(@RequestParam("token") String confirmationToken) {
      return emailConfirmationService.confirmEmail(confirmationToken);
   }

   @GetMapping(value = "/logout")
   public ResponseEntity<?> logout(HttpServletResponse response, HttpServletRequest request) {
      return cookieService.removeCookies(response, request);
   }

   @ExceptionHandler({BadRequestException.class})
   public ResponseEntity<String> handleInvalidTopTalentDataException(BadRequestException e) {
      return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
   }
}
