package com.Cinema.security.auth;

import com.Cinema.security.auth.request.AuthenticationRequest;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

   @Autowired
   private AuthenticationService authenticationService;
   @Value("${cookie.expiration.time}")
   private int EXPIRATION_TIME;

   public Cookie createJwtCookie(AuthenticationRequest request) {
      final Cookie jwtCookie = new Cookie("jwt", authenticationService.authenticate(request));
      jwtCookie.setHttpOnly(true);
      jwtCookie.setPath("/");
      jwtCookie.setMaxAge(EXPIRATION_TIME);
      return jwtCookie;
   }

   public Cookie createUserCookie() {
      final Cookie userCookie = new Cookie("logedIn", "true");
      userCookie.setPath("/");
      userCookie.setMaxAge(EXPIRATION_TIME);
      return userCookie;
   }


}
