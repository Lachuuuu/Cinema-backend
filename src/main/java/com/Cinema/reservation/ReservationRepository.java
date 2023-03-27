package com.Cinema.reservation;

import com.Cinema.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
   Set<Reservation> findAllByUser(User user);
}
