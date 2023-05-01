package com.Cinema.showing.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddShowingRequest {

   @NotNull(message = "movie id cannot be null")
   private Long movieId;

   @NotNull(message = "hall id cannot be null")
   private Long hallId;

   private LocalDateTime showingStartTime;

   @NotBlank(message = "showing name cannot be blank")
   private String showingName;

}
