package com.Cinema.security.auth;

import com.Cinema.security.auth.request.AuthenticationRequest;
import com.Cinema.security.auth.request.RegisterRequest;
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

   @Autowired
   private CookieService cookieService;

   @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> register(
         @RequestBody RegisterRequest request
   ) {
      return ResponseEntity.ok(authenticationService.register(request));
   }

   @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<Void> auth(
         @RequestBody AuthenticationRequest request,
         HttpServletResponse response
   ) {
      response.addCookie(cookieService.createJwtCookie(request));
      response.addCookie(cookieService.createUserCookie(request));
      return ResponseEntity.ok().build();
   }
}
