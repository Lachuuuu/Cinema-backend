package com.Cinema.user;

import com.Cinema.security.auth.CookieService;
import com.Cinema.security.auth.exception.BadRequestException;
import com.Cinema.user.dto.UserDto;
import com.Cinema.user.request.UpdateEmailRequest;
import com.Cinema.user.request.UpdateNameRequest;
import com.Cinema.user.request.UpdatePasswordRequest;
import com.Cinema.user.request.UpdatePhoneNumberRequest;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

   private final UserService userService;

   private final UserAssembler userAssembler;

   private final Gson gson;

   private final CookieService cookieService;

   private final PasswordEncoder passwordEncoder;

   @GetMapping(value = "/userByToken", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> getUser(
         @CookieValue(name = "jwt") String token
   ) {
      User user = userService.getUser(token);
      if (user == null) return ResponseEntity.badRequest().body(null);

      UserDto userDto = userAssembler.toUserDto(user);
      return ResponseEntity.ok(userDto);
   }

   @PostMapping(value = "/change/email", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> changeEmail(
         @RequestBody UpdateEmailRequest updateEmailRequest,
         @CookieValue(name = "jwt") String token,
         HttpServletResponse response,
         HttpServletRequest request
   ) throws BadRequestException, UsernameNotFoundException {
      User user = userService.getUser(token);
      if (userService.changeEmail(user, updateEmailRequest) == null)
         throw new BadRequestException("could not change email");
      return cookieService.removeCookies(response, request);
   }

   @PostMapping(value = "/change/password", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> changePassword(
         @RequestBody UpdatePasswordRequest updatePasswordRequest,
         @CookieValue(name = "jwt") String token,
         HttpServletResponse response,
         HttpServletRequest request
   ) throws BadRequestException, UsernameNotFoundException {
      User user = userService.getUser(token);
      if (userService.changePassword(user, updatePasswordRequest) == null)
         throw new BadRequestException("could not change password");
      return ResponseEntity.ok().body(gson.toJson("Password changed successfully"));
   }

   @PostMapping(value = "/change/first_name", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> changeFirstName(
         @RequestBody UpdateNameRequest updateNameRequest,
         @CookieValue(name = "jwt") String token,
         HttpServletResponse response,
         HttpServletRequest request
   ) throws BadRequestException, UsernameNotFoundException {
      User user = userService.getUser(token);
      if (userService.changeFirstName(user, updateNameRequest) == null)
         throw new BadRequestException("could not change first name");
      return ResponseEntity.ok().body(gson.toJson("first name changed successfully"));
   }

   @PostMapping(value = "/change/last_name", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> changeLastName(
         @RequestBody UpdateNameRequest updateNameRequest,
         @CookieValue(name = "jwt") String token,
         HttpServletResponse response,
         HttpServletRequest request
   ) throws BadRequestException, UsernameNotFoundException {
      User user = userService.getUser(token);
      if (userService.changeLastName(user, updateNameRequest) == null)
         throw new BadRequestException("could not change last name");
      return ResponseEntity.ok().body(gson.toJson("last name changed successfully"));
   }

   @PostMapping(value = "/change/phone", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> changeLastName(
         @RequestBody UpdatePhoneNumberRequest updatePhoneNumberRequest,
         @CookieValue(name = "jwt") String token,
         HttpServletResponse response,
         HttpServletRequest request
   ) throws BadRequestException, UsernameNotFoundException {
      User user = userService.getUser(token);
      if (userService.changePhoneNumber(user, updatePhoneNumberRequest) == null)
         throw new BadRequestException("could not change phone number");
      return ResponseEntity.ok().body(gson.toJson("phone number changed successfully"));
   }

   @ExceptionHandler({BadRequestException.class, UsernameNotFoundException.class})
   public ResponseEntity<String> handleInvalidTopTalentDataException(Exception e) {
      return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
   }
}
