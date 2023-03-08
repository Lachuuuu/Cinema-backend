package com.Cinema.security.auth;

import com.Cinema.security.auth.request.AuthenticationRequest;
import com.Cinema.security.auth.request.RegisterRequest;
import com.Cinema.security.auth.verification.EmailConfirmationService;
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

   @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> register(
         @Valid @RequestBody RegisterRequest request
   ) {
      return emailConfirmationService.sendEmail(authenticationService.register(request));
   }

   @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<Void> auth(
         @RequestBody AuthenticationRequest request,
         HttpServletResponse response
   ) {
      response.addCookie(cookieService.createJwtCookie(request));
      response.addCookie(cookieService.createUserCookie());
      return ResponseEntity.ok().build();
   }

   @GetMapping(value = "/confirm-account")
   public ResponseEntity<?> confirmUserAccount(@RequestParam("token") String confirmationToken) {
      return emailConfirmationService.confirmEmail(confirmationToken);
   }
}
