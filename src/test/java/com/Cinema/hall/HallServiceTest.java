package com.Cinema.hall;

import com.Cinema.hall.request.AddHallRequest;
import com.Cinema.security.auth.exception.BadRequestException;
import com.Cinema.showing.Showing;
import com.Cinema.showing.ShowingRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class HallServiceTest {
   @Mock
   HallRepository hallRepository;

   @Mock
   ShowingRepository showingRepository;

   @InjectMocks
   HallService hallService;


   @Test
   void addHall_Should_Return_Exception_When_Name_Is_Null_And_SeatsMap_Is_Null() {
      // given
      AddHallRequest addHallRequest = new AddHallRequest();
      addHallRequest.setName(null);
      addHallRequest.setSeatsMap(null);

      // then
      BadRequestException exception = assertThrows(BadRequestException.class, () -> hallService.addHall(addHallRequest));
      assertEquals("Seats map and name cannot be empty", exception.getMessage());
   }

   @Test
   void addHall_Should_Return_Exception_When_Name_Is_Null_OR_SeatsMap_Is_Null() {
      // given
      AddHallRequest addHallRequest = new AddHallRequest();
      addHallRequest.setName("name");
      addHallRequest.setSeatsMap(null);

      AddHallRequest addHallRequest2 = new AddHallRequest();
      addHallRequest.setName(null);
      addHallRequest.setSeatsMap("101|101");

      // then
      BadRequestException exception = assertThrows(BadRequestException.class, () -> hallService.addHall(addHallRequest));
      BadRequestException exception2 = assertThrows(BadRequestException.class, () -> hallService.addHall(addHallRequest2));

      assertEquals("Seats map and name cannot be empty", exception.getMessage());
      assertEquals("Seats map and name cannot be empty", exception2.getMessage());
   }

   @Test
   void addHall_Should_Return_Hall_When_Name_And_SeatsMap_Is_Not_Null() throws BadRequestException {
      // given
      AddHallRequest addHallRequest = new AddHallRequest();
      addHallRequest.setName("name");
      addHallRequest.setSeatsMap("101|101");
      // when
      when(hallRepository.save(any(Hall.class))).thenReturn(new Hall());
      // then
      assertEquals(Hall.class, hallService.addHall(addHallRequest).getClass());
   }

   @Test
   void removeHall_Should_Return_Exception_When_Hall_Not_Found() {
      //given
      Long hallId = 1L;
      //when
      when(hallRepository.findById(any())).thenReturn(Optional.empty());
      //then
      BadRequestException exception = assertThrows(BadRequestException.class, () -> hallService.removeHall(hallId));
      assertEquals("Hall not found", exception.getMessage());
   }

   @Test
   void removeHall_Should_Return_Exception_When_There_Are_Showings_In_The_Hall() {
      //given
      Long hallId = 1L;
      Hall hall = new Hall(1L, "", "");
      //when
      when(hallRepository.findById(any(Long.class))).thenReturn(Optional.of(hall));
      when(showingRepository.findAllByHallAndIsActive(hall, true)).thenReturn(List.of(new Showing(null, null, hall, null, null, null, true, null)));
      //then
      BadRequestException exception = assertThrows(BadRequestException.class, () -> hallService.removeHall(hallId));
      assertEquals("There are showings in the hall, delete them first", exception.getMessage());
   }

   @Test
   void getAllHalls_Should_Return_All_Halls() {
      //given
      List<Hall> halls = List.of(new Hall(1L, "101|101", "duza"), new Hall(2L, "10|10", "mala"));
      //when
      when(hallRepository.findAll()).thenReturn(halls);
      //then
      assertEquals(halls, hallService.getAllHalls());
   }
}