package com.Cinema.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

   @Autowired
   private AuthenticationService authenticationService;

   @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<AuthenticationResponse> register(
         @RequestBody RegisterRequest request
   ) {
      return ResponseEntity.ok(authenticationService.register(request));
   }

   @PostMapping("/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<AuthenticationResponse> register(
         @RequestBody AuthenticationRequest request
   ) {
      return ResponseEntity.ok(authenticationService.authenticate(request));
   }
}
