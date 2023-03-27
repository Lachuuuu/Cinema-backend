package com.Cinema.showing;

import com.Cinema.security.auth.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ShowingService {

   private final ShowingRepository showingRepository;

   public Showing updateSeatsMap(Showing showing, List<Long> RequestedSeats) throws BadRequestException {
      String showingSeatsMap = showing.getSeatsMap();
      StringBuilder newSeatsMap = new StringBuilder(showingSeatsMap);
      for (Long requestedSeat : RequestedSeats) {
         if (!(showingSeatsMap.charAt(requestedSeat.intValue()) == '1')) {
            throw new BadRequestException("seat is not availbe");
         }
         newSeatsMap.setCharAt(requestedSeat.intValue(), '2');
      }

      showing.setSeatsMap(String.valueOf(newSeatsMap));
      return showingRepository.save(showing);

   }

   ;

}
