package com.Cinema.movie;

import com.Cinema.movie.dto.MovieDto;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class MovieAssembler {

   public MovieDto toMovieDto(Movie movie) {
      return new MovieDto(
            movie.getId(),
            movie.getName(),
            movie.getDescription(),
            movie.getDurationMin(),
            convertDate(movie.getPremiereDate()),
            movie.getGenres(),
            movie.getMinAge(),
            "data:image/jpeg;base64," + Base64.encodeBase64String(movie.getImage())
      );
   }

   private String convertDate(LocalDate date) {
      DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE;
      return date.format(dtf);
   }

}
