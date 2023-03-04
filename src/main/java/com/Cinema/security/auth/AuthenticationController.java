package com.Cinema.security.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

   @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<Void> auth(
         @RequestBody AuthenticationRequest request,
         HttpServletResponse response
   ) {
      final Cookie jwtCookie = new Cookie("jwt", authenticationService.authenticate(request).getToken());
      jwtCookie.setHttpOnly(true);
      jwtCookie.setPath("/");
      jwtCookie.setMaxAge(10 * 60);
      response.addCookie(jwtCookie);

      final Cookie loogedCookie = new Cookie("logedIn", "true");
      loogedCookie.setPath("/");
      loogedCookie.setMaxAge(10 * 60);

      response.addCookie(loogedCookie);
      return ResponseEntity.ok().build();
   }
}
