package com.Cinema.showing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowingDto {

   private Long id;
   private String movie;
   private Long hall;
   private LocalDateTime showingStartTime;
   private String seatsMap;
   private String name;
}
