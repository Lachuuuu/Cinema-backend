package com.Cinema.user;

import com.Cinema.Validation.interfaces.CorrectRegisterDate;
import com.Cinema.user.userRole.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class User implements UserDetails {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message = "wrong email address")
   @Column(name = "email", unique = true)
   private String email;

   @NotBlank(message = "first name cannot be empty")
   @Size(min = 2, message = "first name should contain atleast 2 signs")
   @Column(name = "firstName")
   private String firstName;

   @NotBlank(message = "last name cannot be empty")
   @Size(min = 2, message = "last name should contain atleast 2 signs")
   @Column(name = "lastName")
   private String lastName;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   @NotNull(message = "birth date cannot be empty")
   @Column(name = "birthDate")
   @CorrectRegisterDate
   private LocalDate birthDate;

   @NotBlank(message = "phone number cannot be empty")
   @Size(min = 9, max = 9, message = "invalid length of phone number")
   @Column(name = "phoneNumber", length = 9, unique = true)
   private String phoneNumber;


   @Column(name = "password")
   private String password;

   @ManyToMany(fetch = FetchType.EAGER)
   @NotNull
   @JoinTable(name = "account_roles",
         joinColumns = @JoinColumn(name = "account_id"),
         inverseJoinColumns = @JoinColumn(name = "role_id"))
   @Column(name = "role")
   private Set<UserRole> roles = new HashSet<>();

   @NotNull
   @Column(name = "isActive")
   private boolean isActive = false;

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      final Set<SimpleGrantedAuthority> authorities = new HashSet<>();

      roles.forEach(it -> authorities.add(new SimpleGrantedAuthority(it.getName())));

      return authorities;
   }

   @Override
   public String getUsername() {
      return email;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return isActive;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }


}
