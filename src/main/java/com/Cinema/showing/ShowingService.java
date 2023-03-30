package com.Cinema.showing;

import com.Cinema.hall.Hall;
import com.Cinema.hall.HallRepository;
import com.Cinema.movie.Movie;
import com.Cinema.movie.MovieRepository;
import com.Cinema.security.auth.exception.BadRequestException;
import com.Cinema.showing.request.AddShowingRequest;
import com.Cinema.showing.request.UpdateShowingRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ShowingService {

   private final ShowingRepository showingRepository;
   private final MovieRepository movieRepository;
   private final HallRepository hallRepository;

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

   public Showing addShowing(AddShowingRequest addShowingRequest) throws BadRequestException {
      final Movie movie = movieRepository.findById(addShowingRequest.getMovieId()).orElseThrow(() -> new BadRequestException("Movie not found"));
      final Hall hall = hallRepository.findById(addShowingRequest.getHallId()).orElseThrow(() -> new BadRequestException("Hall not found"));

      final Showing newShowing = new Showing(null,
            movie,
            hall,
            null,
            addShowingRequest.getShowingStartTime(),
            hall.getSeatsMap(),
            true,
            addShowingRequest.getShowingName()
      );

      return showingRepository.save(newShowing);
   }

   public void removeShowing(Long showingId) throws BadRequestException {
      final Showing showing = showingRepository.findById(showingId)
            .orElseThrow(() -> new BadRequestException("Showing not found"));
      showing.setIsActive(false);

      try {
         showingRepository.save(showing);
      } catch (Exception e) {
         throw new BadRequestException("Could not delete showing");
      }
   }

   public Set<Showing> getAllShowings() {
      Set<Showing> showings = showingRepository.findAll().stream().collect(Collectors.toSet());
      return showings;
   }

   public Showing updateShowing(UpdateShowingRequest updateShowingRequest) throws BadRequestException {
      Showing showing = showingRepository.findById(updateShowingRequest.getShowingId())
            .orElseThrow(() -> new BadRequestException("Showing not found"));

      if (updateShowingRequest.getHallId() != null) {
         if (showing.getReservations().isEmpty()) {
            Hall hall = hallRepository.findById(updateShowingRequest.getHallId())
                  .orElseThrow(() -> new BadRequestException("Hall not found"));
            if (hall != showing.getHall()) {
               showing.setHall(hall);
               showing.setSeatsMap(hall.getSeatsMap());
            }
         } else {
            throw new BadRequestException("You can't change hall when there are reservations");
         }
      }
      if (updateShowingRequest.getMovieId() != null) {
         Movie movie = movieRepository.findById(updateShowingRequest.getMovieId())
               .orElseThrow(() -> new BadRequestException("Movie not found"));
         if (movie != showing.getMovie())
            showing.setMovie(movie);
      }
      if (updateShowingRequest.getName() != null) {
         showing.setName(updateShowingRequest.getName());
      }
      if (updateShowingRequest.getShowingStartTime() != null) {
         showing.setShowingStartTime(updateShowingRequest.getShowingStartTime());
      }

      return showingRepository.save(showing);
   }


}
