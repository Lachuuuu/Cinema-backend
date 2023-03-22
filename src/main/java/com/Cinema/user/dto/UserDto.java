package com.Cinema.user.dto;

import com.Cinema.user.userRole.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
   private String email;

   private String firstName;

   private String lastName;

   private LocalDate birthDate;

   private String phoneNumber;

   private Set<UserRole> roles;
}
