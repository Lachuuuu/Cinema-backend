package com.Cinema.hall;

import com.Cinema.hall.request.AddHallRequest;
import com.Cinema.security.auth.exception.BadRequestException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hall")
public class HallController {
   private final HallService hallService;

   private final Gson gson;

   @GetMapping(value = "/admin/all", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<Hall>> getAll() {
      List<Hall> halls = hallService.getAll();
      return ResponseEntity.ok().body(halls);
   }

   @PostMapping(value = "/admin/add")
   public ResponseEntity<Hall> add(@RequestBody AddHallRequest addHallRequest) throws BadRequestException {
      Hall hall = hallService.add(addHallRequest);
      return ResponseEntity.ok(hall);
   }

   @DeleteMapping(value = "/admin/remove/{hallId}")
   public ResponseEntity<List<Hall>> remove(@PathVariable Long hallId) throws BadRequestException {
      List<Hall> halls = hallService.remove(hallId);
      return ResponseEntity.ok(halls);
   }

   @ExceptionHandler({BadRequestException.class})
   public ResponseEntity<String> exceptionsHandler(BadRequestException e) {
      return ResponseEntity.badRequest().body(gson.toJson(e.getMessage()));
   }

}
