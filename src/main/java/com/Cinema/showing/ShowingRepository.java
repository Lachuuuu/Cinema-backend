package com.Cinema.showing;

import com.Cinema.hall.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowingRepository extends JpaRepository<Showing, Long> {
   List<Showing> findAllByIsActive(Boolean isActive);

   List<Showing> findAllByHallAndIsActive(Hall hall, Boolean isActive);
}
