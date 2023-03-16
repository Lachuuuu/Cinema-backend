package com.Cinema.security.auth.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
   private String email;
   private String firstName;
   private String lastName;
   private LocalDate bDate;
   private String phoneNumber;
   private String password;
}
