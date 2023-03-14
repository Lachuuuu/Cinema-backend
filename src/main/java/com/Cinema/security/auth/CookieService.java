package com.Cinema.security.auth;

import com.Cinema.security.auth.request.AuthenticationRequest;
import com.Cinema.user.User;
import com.Cinema.user.UserAssembler;
import com.Cinema.user.UserRepository;
import com.Cinema.user.dto.UserDto;
import com.Cinema.user.userRole.UserRole;
import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CookieService {

   @Autowired
   private AuthenticationService authenticationService;
   @Value("${cookie.expiration.time}")
   private int EXPIRATION_TIME;

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private UserAssembler userAssembler;

   @Autowired
   private Gson gson;

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

         return ResponseEntity.ok("Successfully Authenticated");
      }
      return ResponseEntity.ok("Cannot Authenticate");
   }

   private List<Cookie> createUserCookies(User user) {
      List<Cookie> cookies = new ArrayList<>();
      HashMap<String, String> userCookies = new HashMap<>();
      UserDto userDto = userAssembler.toUserDto(user);

      userCookies.put("bDate", userDto.getbDate().toString());
      userCookies.put("email", userDto.getEmail());
      userCookies.put("firstName", userDto.getFirstName());
      userCookies.put("lastName", userDto.getLastName());
      userCookies.put("phoneNumber", userDto.getPhoneNumber());
      userCookies.put("roles", parseUserRoles(userDto.getRoles()));

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

   private Cookie createJwtCookie(AuthenticationRequest request) {
      final Cookie jwtCookie = new Cookie("jwt", authenticationService.authenticate(request));
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
