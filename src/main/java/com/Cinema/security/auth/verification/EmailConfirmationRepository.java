package com.Cinema.security.auth.verification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailConfirmationRepository extends JpaRepository<EmailConfirmationToken, Long> {
   EmailConfirmationToken findByConfirmationToken(String confirmationToken);
}
