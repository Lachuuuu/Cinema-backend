package com.Cinema.security.auth.verification;

import com.Cinema.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "emailConfirmationToken")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}
