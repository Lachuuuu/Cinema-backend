package com.Cinema.security.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
   @NotBlank(message = "email cannot be blank")
   @Email(message = "wrong email")
   private String email;
   @NotBlank(message = "first name cannot be blank")
   private String firstName;
   @NotBlank(message = "last name cannot be blank")
   private String lastName;
   private LocalDate birthDate;
   @NotBlank(message = "phone number cannot be blank")
   private String phoneNumber;

   @Size(min = 4, message = "password should have atleast 4 signs")
   private String password;
}
