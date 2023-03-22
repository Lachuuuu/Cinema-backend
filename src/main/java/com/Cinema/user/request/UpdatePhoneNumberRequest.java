package com.Cinema.user.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePhoneNumberRequest {
   private String oldValue;
   @Size(min = 9, max = 9, message = "invalid length of phone number")
   private String newValue;
}
