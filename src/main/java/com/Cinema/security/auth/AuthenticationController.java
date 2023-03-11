package com.Cinema.security.auth;

import com.Cinema.security.auth.exception.BadRequestException;
import com.Cinema.security.auth.request.AuthenticationRequest;
import com.Cinema.security.auth.request.RegisterRequest;
import com.Cinema.security.auth.verification.EmailConfirmationService;
import com.Cinema.user.User;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

   @Autowired
   private AuthenticationService authenticationService;
   @Autowired
   private CookieService cookieService;
   @Autowired
   private EmailConfirmationService emailConfirmationService;

   @Autowired
   private Gson gson;

   @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> register(
         @Valid @RequestBody RegisterRequest request
   ) {
      User user;
      try {
         user = authenticationService.register(request);
      } catch (BadRequestException e) {
         return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
      }
      emailConfirmationService.sendEmail(user);
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
}
