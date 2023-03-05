package com.Cinema.security.auth.request;

import com.Cinema.user.userRole.UserRole;

import java.time.LocalDate;
import java.util.Set;

public class RegisterRequest {

   private String email;

   private String password;

   private String firstName;

   private String lastName;

   private LocalDate bDate;

   private String phoneNumber;

   public RegisterRequest() {
   }

   public RegisterRequest(String email, String password, String firstName, String lastName, LocalDate bDate, String phoneNumber, Set<UserRole> roles) {
      this.email = email;
      this.password = password;
      this.firstName = firstName;
      this.lastName = lastName;
      this.bDate = bDate;
      this.phoneNumber = phoneNumber;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
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

}
