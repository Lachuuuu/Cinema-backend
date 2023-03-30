package com.Cinema.hall;

import com.Cinema.hall.request.AddHallRequest;
import com.Cinema.security.auth.exception.BadRequestException;
import com.Cinema.showing.Showing;
import com.Cinema.showing.ShowingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class HallService {

   private final HallRepository hallRepository;

   private final ShowingRepository showingRepository;

   public Hall addHall(AddHallRequest addHallRequest) throws BadRequestException {
      if (addHallRequest.getSeatsMap() != null && addHallRequest.getName() != null) {
         Hall hall = new Hall(null, addHallRequest.getSeatsMap(), addHallRequest.getName());
         return hallRepository.save(hall);
      } else throw new BadRequestException("Seats map cannot be empty");
   }

   public void removeHall(Long hallId) throws BadRequestException {
      Hall hall = hallRepository.findById(hallId)
            .orElseThrow(() -> new BadRequestException("Hall not found"));

      Set<Showing> showings = showingRepository.findAll().stream()
            .filter(it -> (it.getHall().equals(hall) && it.getIsActive()))
            .collect(Collectors.toSet());
      if (showings.isEmpty()) hallRepository.delete(hall);
      else throw new BadRequestException("There are showings in the hall, delete them first");
   }

   public Set<Hall> getAllHalls() {
      Set<Hall> halls = hallRepository.findAll().stream().collect(Collectors.toSet());
      return halls;
   }

}
