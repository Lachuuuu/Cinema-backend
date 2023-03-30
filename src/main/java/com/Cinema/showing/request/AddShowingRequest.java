package com.Cinema.showing.request;

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

   private Long movieId;

   private Long hallId;

   private LocalDateTime showingStartTime;

   private String showingName;

}
