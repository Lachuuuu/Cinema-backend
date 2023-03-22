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
public class UpdateNameRequest {

   private String oldValue;
   @Size(min = 2, message = "first / last name should contain atleast 2 signs")
   private String newValue;
}
