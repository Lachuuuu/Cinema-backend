package com.Cinema.showing;

import com.Cinema.security.auth.exception.BadRequestException;
import com.Cinema.showing.dto.ShowingDto;
import com.Cinema.showing.request.AddShowingRequest;
import com.Cinema.showing.request.UpdateShowingRequest;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/showing")
public class ShowingController {

   private final Gson gson;

   private final ShowingService showingService;

   private final ShowingAssembler showingAssembler;

   @PostMapping(value = "/admin/add", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity addShowing(
         @RequestBody AddShowingRequest addShowingRequest
   ) throws BadRequestException {
      showingService.addShowing(addShowingRequest);
      return ResponseEntity.ok().body(null);
   }

   @PutMapping(value = "/admin/remove/{showingId}")
   public ResponseEntity removeShowing(@PathVariable Long showingId) throws BadRequestException {
      showingService.removeShowing(showingId);
      return ResponseEntity.ok().body(null);
   }

   @PutMapping(value = "/admin/update")
   public ResponseEntity<Showing> updateShowing(@RequestBody UpdateShowingRequest updateShowingRequest) throws BadRequestException {
      Showing showing = showingService.updateShowing(updateShowingRequest);
      return ResponseEntity.ok(showing);
   }

   @GetMapping(value = "/admin/all")
   public ResponseEntity<Set<ShowingDto>> getAllShowings() {
      Set<ShowingDto> showings = showingService.getAllShowings().stream()
            .filter(it -> it.getIsActive())
            .map(it -> showingAssembler.toShowingDto(it))
            .collect(Collectors.toSet());
      return ResponseEntity.ok().body(showings);
   }

   @ExceptionHandler({BadRequestException.class})
   public ResponseEntity<String> handleInvalidTopTalentDataException(Exception e) {
      return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
   }
}
