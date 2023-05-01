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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

   private final UserService userService;

   private final UserAssembler userAssembler;

   private final Gson gson;

   private final CookieService cookieService;

   @GetMapping(value = "/userByToken", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<UserDto> getUserByToken(
         @CookieValue(name = "jwt") String token
   ) throws BadRequestException {
      User user = userService.getUserByToken(token);
      return ResponseEntity.ok(userAssembler.toDto(user));
   }

   @PostMapping(value = "/change/email", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> changeEmail(
         @RequestBody @Valid UpdateEmailRequest updateEmailRequest,
         @CookieValue(name = "jwt") String token,
         HttpServletResponse response,
         HttpServletRequest request
   ) throws BadRequestException {
      User user = userService.getUserByToken(token);
      userService.changeEmail(user, updateEmailRequest);
      cookieService.removeCookies(response, request);
      return ResponseEntity.ok(gson.toJson("Email changed successfully, you will be directed to login page in 5 seconds"));
   }

   @PostMapping(value = "/change/password", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> changePassword(
         @RequestBody @Valid UpdatePasswordRequest updatePasswordRequest,
         @CookieValue(name = "jwt") String token
   ) throws BadRequestException {
      User user = userService.getUserByToken(token);
      userService.changePassword(user, updatePasswordRequest);
      return ResponseEntity.ok().body(gson.toJson("Password changed successfully"));
   }

   @PostMapping(value = "/change/first_name", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<UserDto> changeFirstName(
         @RequestBody @Valid UpdateNameRequest updateNameRequest,
         @CookieValue(name = "jwt") String token
   ) throws BadRequestException {
      User user = userService.changeFirstName(userService.getUserByToken(token), updateNameRequest);
      return ResponseEntity.ok().body(userAssembler.toDto(user));
   }

   @PostMapping(value = "/change/last_name", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<UserDto> changeLastName(
         @RequestBody @Valid UpdateNameRequest updateNameRequest,
         @CookieValue(name = "jwt") String token
   ) throws BadRequestException {
      User user = userService.changeLastName(userService.getUserByToken(token), updateNameRequest);
      return ResponseEntity.ok().body(userAssembler.toDto(user));
   }

   @PostMapping(value = "/change/phone", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> changeLastName(
         @RequestBody @Valid UpdatePhoneNumberRequest updatePhoneNumberRequest,
         @CookieValue(name = "jwt") String token
   ) throws BadRequestException {
      User user = userService.getUserByToken(token);
      userService.changePhoneNumber(user, updatePhoneNumberRequest);
      return ResponseEntity.ok().body(gson.toJson("phone number changed successfully"));
   }

   @PostMapping(value = "/remove")
   public ResponseEntity<User> deactivateAccount(@CookieValue(name = "jwt") String token) throws BadRequestException {
      User user = userService.getUserByToken(token);
      return ResponseEntity.ok(userService.deactivateAccount(user));
   }

   @ExceptionHandler({BadRequestException.class})
   public ResponseEntity<String> exceptionsHandler(Exception e) {
      return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
   }

   @ExceptionHandler({MethodArgumentNotValidException.class})
   public ResponseEntity<String> exceptionsHandlerForValidation(MethodArgumentNotValidException exception) {
      BindingResult result = exception.getBindingResult();
      List<FieldError> fieldErrors = result.getFieldErrors();
      return ResponseEntity.badRequest()
            .body(
                  gson.toJson(
                        fieldErrors.stream()
                              .map(DefaultMessageSourceResolvable::getDefaultMessage)
                              .collect(Collectors.joining(""))
                  )
            );
   }
}
