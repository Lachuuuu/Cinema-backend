package com.Cinema.user.dto;

import com.Cinema.user.userRole.UserRole;

import java.time.LocalDate;
import java.util.Set;

public class UserDto {
   private String email;

   private String firstName;

   private String lastName;

   private LocalDate bDate;

   private String phoneNumber;

   private Set<UserRole> roles;

   public UserDto(String email, String firstName, String lastName, LocalDate bDate, String phoneNumber, Set<UserRole> roles) {
      this.email = email;
      this.firstName = firstName;
      this.lastName = lastName;
      this.bDate = bDate;
      this.phoneNumber = phoneNumber;
      this.roles = roles;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public LocalDate getbDate() {
      return bDate;
   }

   public void setbDate(LocalDate bDate) {
      this.bDate = bDate;
   }

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   public Set<UserRole> getRoles() {
      return roles;
   }

   public void setRoles(Set<UserRole> roles) {
      this.roles = roles;
   }
}
