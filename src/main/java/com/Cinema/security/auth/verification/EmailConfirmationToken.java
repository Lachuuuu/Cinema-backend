package com.Cinema.security.auth.verification;

import com.Cinema.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity(name = "emailConfirmationToken")
public class EmailConfirmationToken {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "tokenId")
   private Long tokenId;

   @Column(name = "confirmationToken")
   private String confirmationToken;

   @Column(name = "expDate")
   private LocalDate expDate;

   @ManyToOne()
   @JoinColumn(name = "account_id")
   private User user;

   public EmailConfirmationToken() {
   }

   public EmailConfirmationToken(Long tokenId, String confirmationToken, LocalDate expDate, User user) {
      this.tokenId = tokenId;
      this.confirmationToken = confirmationToken;
      this.expDate = expDate;
      this.user = user;
   }

   public Long getTokenId() {
      return tokenId;
   }

   public void setTokenId(Long tokenId) {
      this.tokenId = tokenId;
   }

   public String getConfirmationToken() {
      return confirmationToken;
   }

   public void setConfirmationToken(String confirmationToken) {
      this.confirmationToken = confirmationToken;
   }

   public LocalDate getExpDate() {
      return expDate;
   }

   public void setExpDate(LocalDate expDate) {
      this.expDate = expDate;
   }

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }
}
