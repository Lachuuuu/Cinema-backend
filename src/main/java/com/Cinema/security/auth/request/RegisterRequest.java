package com.Cinema.security.auth.request;

import jakarta.validation.constraints.Size;
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
   private LocalDate birthDate;
   private String phoneNumber;

   @Size(min = 4, message = "password should have atleast 4 signs")
   private String password;
}
