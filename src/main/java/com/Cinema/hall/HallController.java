package com.Cinema.hall;

import com.Cinema.hall.request.AddHallRequest;
import com.Cinema.security.auth.exception.BadRequestException;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/hall")
public class HallController {
   private final HallService hallService;

   private final Gson gson;

   @GetMapping(value = "/admin/all", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<Hall>> getAllHalls() {
      List<Hall> halls = hallService.getAllHalls();
      return ResponseEntity.ok().body(halls);
   }

   @PostMapping(value = "/admin/add")
   public ResponseEntity addHall(@RequestBody AddHallRequest addHallRequest) throws BadRequestException {
      Hall hall = hallService.addHall(addHallRequest);
      return ResponseEntity.ok(hall);
   }

   @DeleteMapping(value = "/admin/remove/{hallId}")
   public ResponseEntity removeHall(@PathVariable Long hallId) throws BadRequestException {
      List<Hall> halls = hallService.removeHall(hallId);
      return ResponseEntity.ok(halls);
   }

   @ExceptionHandler({BadRequestException.class})
   public ResponseEntity<String> handleInvalidTopTalentDataException(BadRequestException e) {
      return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
   }

}
