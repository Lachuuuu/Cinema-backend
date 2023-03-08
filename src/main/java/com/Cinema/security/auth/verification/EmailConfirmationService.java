package com.Cinema.security.auth.verification;

import com.Cinema.user.User;
import com.Cinema.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional
public class EmailConfirmationService {

   @Autowired
   private JavaMailSender javaMailSender;
   @Autowired
   private EmailConfirmationRepository emailConfirmationRepository;
   @Autowired
   private UserRepository userRepository;

   public ResponseEntity<?> sendEmail(User user) {
      String token = UUID.randomUUID().toString();
      EmailConfirmationToken confirmationToken = new EmailConfirmationToken(null, token, LocalDate.now().plusDays(1), user);

      emailConfirmationRepository.save(confirmationToken);

      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setTo(user.getEmail());
      mailMessage.setSubject("Complete Registration!");
      mailMessage.setText("To confirm your account, please click here : "
            + "http://localhost:8080/auth/confirm-account?token=" + confirmationToken.getConfirmationToken());
      javaMailSender.send(mailMessage);

      return ResponseEntity.ok("Verify email by the link sent on your email address");
   }

   public ResponseEntity<?> confirmEmail(String confirmationToken) {

      EmailConfirmationToken token = emailConfirmationRepository.findByConfirmationToken(confirmationToken);

      if (token != null) {
         User user = token.getUser();
         user.setActive(true);
         userRepository.save(user);
         emailConfirmationRepository.delete(token);
         return ResponseEntity.ok("Email verified successfully!");
      }
      return ResponseEntity.badRequest().body("Error: Couldn't verify email");
   }


}
