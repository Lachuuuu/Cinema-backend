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
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

   private final UserService userService;

   private final UserAssembler userAssembler;

   private final Gson gson;

   private final CookieService cookieService;

   @GetMapping(value = "/userByToken", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<UserDto> getUser(
         @CookieValue(name = "jwt") String token
   ) throws BadRequestException {
      User user = userService.getUser(token);
      return ResponseEntity.ok(userAssembler.toUserDto(user));
   }

   @PostMapping(value = "/change/email", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> changeEmail(
         @RequestBody UpdateEmailRequest updateEmailRequest,
         @CookieValue(name = "jwt") String token,
         HttpServletResponse response,
         HttpServletRequest request
   ) throws BadRequestException {
      User user = userService.getUser(token);
      userService.changeEmail(user, updateEmailRequest);
      cookieService.removeCookies(response, request);
      return ResponseEntity.ok(gson.toJson("Email changed successfully, you will be directed to login page in 5 seconds"));
   }

   @PostMapping(value = "/change/password", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> changePassword(
         @RequestBody UpdatePasswordRequest updatePasswordRequest,
         @CookieValue(name = "jwt") String token
   ) throws BadRequestException {
      User user = userService.getUser(token);
      userService.changePassword(user, updatePasswordRequest);
      return ResponseEntity.ok().body(gson.toJson("Password changed successfully"));
   }

   @PostMapping(value = "/change/first_name", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<UserDto> changeFirstName(
         @RequestBody UpdateNameRequest updateNameRequest,
         @CookieValue(name = "jwt") String token
   ) throws BadRequestException {
      User user = userService.changeFirstName(userService.getUser(token), updateNameRequest);
      return ResponseEntity.ok().body(userAssembler.toUserDto(user));
   }

   @PostMapping(value = "/change/last_name", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<UserDto> changeLastName(
         @RequestBody UpdateNameRequest updateNameRequest,
         @CookieValue(name = "jwt") String token
   ) throws BadRequestException {
      User user = userService.changeLastName(userService.getUser(token), updateNameRequest);
      return ResponseEntity.ok().body(userAssembler.toUserDto(user));
   }

   @PostMapping(value = "/change/phone", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> changeLastName(
         @RequestBody UpdatePhoneNumberRequest updatePhoneNumberRequest,
         @CookieValue(name = "jwt") String token
   ) throws BadRequestException {
      User user = userService.getUser(token);
      userService.changePhoneNumber(user, updatePhoneNumberRequest);
      return ResponseEntity.ok().body(gson.toJson("phone number changed successfully"));
   }

   @PostMapping(value = "/remove")
   public ResponseEntity<User> deactivateAccount(@CookieValue(name = "jwt") String token) throws BadRequestException {
      User user = userService.getUser(token);
      return ResponseEntity.ok(userService.deactivateAccount(user));
   }

   @ExceptionHandler({BadRequestException.class})
   public ResponseEntity<String> handleInvalidTopTalentDataException(Exception e) {
      return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
   }
}
