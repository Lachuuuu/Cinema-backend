package com.Cinema.security.auth;

import com.Cinema.security.auth.request.AuthenticationRequest;
import com.Cinema.user.User;
import com.Cinema.user.UserAssembler;
import com.Cinema.user.UserRepository;
import com.Cinema.user.dto.UserDto;
import com.Cinema.user.userRole.UserRole;
import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

   public ResponseEntity<?> createAuthenticationCookies(AuthenticationRequest request, HttpServletResponse response) {
      Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

      if (optionalUser.isPresent()) {
         User user = optionalUser.orElse(null);
         final Cookie jwtCookie = createJwtCookie(request);
         List<Cookie> userCookies = createUserCookies(user);

         response.addCookie(jwtCookie);
         userCookies.forEach(it -> response.addCookie(it));

         response.setHeader("Access-Control-Allow-Credentials", String.valueOf(true));
         // response.setHeader("Access-Control-Allow-Origin", "*");

         return ResponseEntity.ok(gson.toJson("Successfully Authenticated"));
      }
      return ResponseEntity.badRequest().body(gson.toJson("Authentication Failed"));
   }

   public ResponseEntity<?> removeCookies(HttpServletResponse response, HttpServletRequest request) {
      final Cookie[] cookies = request.getCookies();
      if (cookies != null) Arrays.stream(cookies).forEach(cookie -> {
         cookie.setMaxAge(0);
         cookie.setValue("");
         cookie.setPath("/");
         cookie.setDomain(null);
         response.addCookie(cookie);
      });
      return ResponseEntity.ok().body(gson.toJson("Successfully logged out"));
   }

   private List<Cookie> createUserCookies(User user) {
      List<Cookie> cookies = new ArrayList<>();
      HashMap<String, String> userCookies = new HashMap<>();
      UserDto userDto = userAssembler.toUserDto(user);

      userCookies.put("firstName", userDto.getFirstName());

      userCookies.forEach((key, value) -> {
         Cookie cookie = new Cookie(key, value);
         cookie.setPath("/");
         cookie.setDomain(null);
         cookie.setMaxAge(EXPIRATION_TIME);
         //cookie.setAttribute("SameSite", "None");
         //cookie.setSecure(true);
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
