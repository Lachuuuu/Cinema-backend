package com.Cinema.showing;

import com.Cinema.showing.dto.ShowingDto;
import org.springframework.stereotype.Service;

@Service
public class ShowingAssembler {

   public ShowingDto toShowingDto(Showing Showing) {
      return new ShowingDto(Showing.getId(),
            Showing.getMovie().getName(),
            Showing.getHall().getId(),
            Showing.getShowingStartTime(),
            Showing.getSeatsMap(),
            Showing.getName());
   }

}
