package com.Cinema.security.auth.verification;

import com.Cinema.user.User;
import com.Cinema.user.UserRepository;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailConfirmationService {

   private final JavaMailSender javaMailSender;

   private final EmailConfirmationRepository emailConfirmationRepository;

   private final UserRepository userRepository;
   @Value("${frontend.url}")
   private String frontendUrl;

   private final Gson gson;


   @Async
   public void sendEmail(User user) {
      String token = UUID.randomUUID().toString();
      EmailConfirmationToken confirmationToken = new EmailConfirmationToken(null, token, LocalDate.now().plusDays(1), user);

      emailConfirmationRepository.save(confirmationToken);

      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setTo(user.getEmail());
      mailMessage.setSubject("Complete Registration!");
      mailMessage.setText("To confirm your account, please click here : "
            + frontendUrl + "confirm-account?token=" + confirmationToken.getConfirmationToken());
      javaMailSender.send(mailMessage);
   }

   public ResponseEntity<?> confirmEmail(String confirmationToken) {

      EmailConfirmationToken token = emailConfirmationRepository.findByConfirmationToken(confirmationToken);

      if (token != null) {
         User user = token.getUser();
         user.setActive(true);
         userRepository.save(user);
         emailConfirmationRepository.delete(token);
         return ResponseEntity.ok(gson.toJson("Email verified successfully!"));
      }
      return ResponseEntity.badRequest().body(gson.toJson("Couldn't verify email"));
   }


}
