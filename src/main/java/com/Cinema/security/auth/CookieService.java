package com.Cinema.security.auth;

import com.Cinema.security.auth.request.AuthenticationRequest;
import com.Cinema.user.User;
import com.Cinema.user.UserAssembler;
import com.Cinema.user.UserRepository;
import com.Cinema.user.userRole.UserRole;
import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CookieService {

   private final AuthenticationService authenticationService;
   @Value("${cookie.expiration.time}")
   private int EXPIRATION_TIME;
   private final UserRepository userRepository;
   private final UserAssembler userAssembler;
   private final Gson gson;

   public User createAuthenticationCookies(AuthenticationRequest request, HttpServletResponse response) {
      Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

      if (optionalUser.isPresent()) {
         User user = optionalUser.orElse(null);
         final Cookie jwtCookie = createJwtCookie(request);
         List<Cookie> userCookies = createUserCookies(user);

         response.addCookie(jwtCookie);
         userCookies.forEach(it -> response.addCookie(it));

         response.setHeader("Access-Control-Allow-Credentials", String.valueOf(true));

         return user;
      }
      return null;
   }

   public void removeCookies(HttpServletResponse response, HttpServletRequest request) {
      final Cookie[] cookies = request.getCookies();
      if (cookies != null) Arrays.stream(cookies).forEach(cookie -> {
         cookie.setMaxAge(0);
         cookie.setValue("");
         cookie.setPath("/");
         cookie.setDomain(null);
         response.addCookie(cookie);
      });
   }

   private List<Cookie> createUserCookies(User user) {
      List<Cookie> cookies = new ArrayList<>();
      HashMap<String, String> userCookies = new HashMap<>();

      userCookies.put("userId", user.getId().toString());

      userCookies.forEach((key, value) -> {
         Cookie cookie = new Cookie(key, value);
         cookie.setPath("/");
         cookie.setDomain(null);
         cookie.setMaxAge(EXPIRATION_TIME);
         cookies.add(cookie);
      });

      return cookies;
   }

   private Cookie createJwtCookie(AuthenticationRequest authRequest) {
      Cookie jwtCookie = new Cookie("jwt", authenticationService.authenticate(authRequest));
      jwtCookie.setHttpOnly(true);
      jwtCookie.setPath("/");
      jwtCookie.setMaxAge(EXPIRATION_TIME);

      return jwtCookie;
   }

   private String parseUserRoles(Set<UserRole> roles) {
      String result = "";
      Iterator<UserRole> rolesIterator = roles.iterator();
      while (rolesIterator.hasNext()) {
         result += rolesIterator.next().getName();
         if (rolesIterator.hasNext()) {
            result += "|";
         }
      }
      return result;
   }


}
