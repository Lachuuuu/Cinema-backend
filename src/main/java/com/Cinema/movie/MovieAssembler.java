package com.Cinema.movie;

import com.Cinema.movie.dto.MovieDto;
import com.Cinema.showing.Showing;
import com.Cinema.showing.ShowingAssembler;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieAssembler {

   private final ShowingAssembler showingAssembler;

   public MovieDto toDto(Movie movie) {
      return new MovieDto(
            movie.getId(),
            movie.getName(),
            movie.getDescription(),
            movie.getDurationMin(),
            convertDate(movie.getPremiereDate()),
            movie.getGenres(),
            movie.getMinAge(),
            "data:image/jpeg;base64," + Base64.encodeBase64String(movie.getImage()),
            movie.getShowings().stream()
                  .filter(Showing::getIsActive)
                  .map(showingAssembler::toDto)
                  .collect(Collectors.toSet())
      );
   }

   private String convertDate(LocalDate date) {
      DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE;
      return date.format(dtf);
   }

}
