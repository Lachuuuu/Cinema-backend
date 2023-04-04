package com.Cinema.security.auth;

import com.Cinema.security.auth.exception.BadRequestException;
import com.Cinema.security.auth.request.AuthenticationRequest;
import com.Cinema.security.auth.request.RegisterRequest;
import com.Cinema.security.auth.verification.EmailConfirmationService;
import com.Cinema.user.User;
import com.Cinema.user.UserAssembler;
import com.Cinema.user.dto.UserDto;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

   private final AuthenticationService authenticationService;
   private final CookieService cookieService;
   private final EmailConfirmationService emailConfirmationService;
   private final Gson gson;
   private final UserAssembler userAssembler;

   @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> register(
         @RequestBody RegisterRequest request
   ) throws BadRequestException {
      User user = authenticationService.register(request);
      if (user != null) emailConfirmationService.send(user);
      return ResponseEntity.ok(gson.toJson("Verify email by the link sent on your email address"));
   }

   @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<UserDto> auth(
         @RequestBody AuthenticationRequest request,
         HttpServletResponse response
   ) throws BadRequestException {
      User authenticatedUser = cookieService.createAuthenticationCookies(request, response);
      if (authenticatedUser == null) throw new BadRequestException("Authentication failed");
      return ResponseEntity.ok(userAssembler.toDto(authenticatedUser));
   }

   @GetMapping(value = "/confirm-account", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> confirmUserAccount(@RequestParam("token") String confirmationToken) throws BadRequestException {
      emailConfirmationService.confirm(confirmationToken);
      return ResponseEntity.ok(gson.toJson("Email verified successfully!"));
   }

   @GetMapping(value = "/logout")
   public ResponseEntity<String> logout(HttpServletResponse response, HttpServletRequest request) {
      cookieService.removeCookies(response, request);
      return ResponseEntity.ok(null);
   }

   @ExceptionHandler({BadRequestException.class})
   public ResponseEntity<String> exceptionsHandler(BadRequestException e) {
      return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
   }
}
