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
public class UpdatePasswordRequest {
   private String oldValue;
   @Size(min = 4, message = "password should have atleast 4 signs")
   private String newValue;
}
