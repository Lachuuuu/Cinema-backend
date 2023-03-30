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
public class UpdateShowingRequest {

   private Long showingId;
   private String name;

   private Long hallId;

   private Long movieId;

   private LocalDateTime showingStartTime;

}
