package com.Cinema.hall;

import com.Cinema.hall.request.AddHallRequest;
import com.Cinema.security.auth.exception.BadRequestException;
import com.Cinema.showing.Showing;
import com.Cinema.showing.ShowingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HallService {

   private final HallRepository hallRepository;

   private final ShowingRepository showingRepository;

   public Hall add(AddHallRequest addHallRequest) throws BadRequestException {
      if (addHallRequest.getSeatsMap() != null && addHallRequest.getName() != null) {
         Hall hall = new Hall(null, addHallRequest.getSeatsMap(), addHallRequest.getName());
         return hallRepository.save(hall);
      } else throw new BadRequestException("Seats map and name cannot be empty");
   }

   public List<Hall> remove(Long hallId) throws BadRequestException {
      Hall hall = hallRepository.findById(hallId)
            .orElseThrow(() -> new BadRequestException("Hall not found"));

      List<Showing> showings = showingRepository.findAllByHallAndIsActive(hall, true);
      if (showings.isEmpty()) hallRepository.delete(hall);
      else throw new BadRequestException("There are showings in the hall, delete them first");
      return hallRepository.findAll();
   }

   public List<Hall> getAll() {
      return hallRepository.findAll();
   }

}
