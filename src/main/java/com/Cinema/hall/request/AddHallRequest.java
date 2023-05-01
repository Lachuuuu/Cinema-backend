package com.Cinema.hall.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddHallRequest {

   @NotBlank(message = "seats map cannot be blank")
   private String seatsMap;

   @NotBlank(message = "hall name cannot be blank")
   private String name;

}
