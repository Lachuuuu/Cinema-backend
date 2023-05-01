package com.Cinema.showing;

import com.Cinema.security.auth.exception.BadRequestException;
import com.Cinema.showing.dto.ShowingDto;
import com.Cinema.showing.request.AddShowingRequest;
import com.Cinema.showing.request.UpdateShowingRequest;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/showing")
public class ShowingController {

   private final Gson gson;

   private final ShowingService showingService;

   @PostMapping(value = "/admin/add", consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> add(
         @RequestBody @Valid AddShowingRequest addShowingRequest
   ) throws BadRequestException {
      showingService.add(addShowingRequest);
      return ResponseEntity.ok().body(gson.toJson("Showing added successfully"));
   }

   @PutMapping(value = "/admin/remove/{showingId}")
   public ResponseEntity<List<Showing>> remove(@PathVariable Long showingId) throws BadRequestException {
      List<Showing> showings = showingService.remove(showingId);
      return ResponseEntity.ok().body(showings);
   }

   @PutMapping(value = "/admin/update")
   public ResponseEntity<Showing> update(@RequestBody @Valid UpdateShowingRequest updateShowingRequest) throws BadRequestException {
      Showing showing = showingService.update(updateShowingRequest);
      return ResponseEntity.ok(showing);
   }

   @GetMapping(value = "/admin/all")
   public ResponseEntity<List<ShowingDto>> getAll() {
      List<ShowingDto> showings = showingService.getAll();
      return ResponseEntity.ok().body(showings);
   }

   @ExceptionHandler({BadRequestException.class})
   public ResponseEntity<String> handleInvalidTopTalentDataException(Exception e) {
      return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
   }

   @ExceptionHandler({MethodArgumentNotValidException.class})
   public ResponseEntity<String> exceptionsHandlerForValidation(MethodArgumentNotValidException exception) {
      BindingResult result = exception.getBindingResult();
      List<FieldError> fieldErrors = result.getFieldErrors();
      return ResponseEntity.badRequest()
            .body(
                  gson.toJson(
                        fieldErrors.stream()
                              .map(DefaultMessageSourceResolvable::getDefaultMessage)
                              .collect(Collectors.joining(""))
                  )
            );
   }
}
