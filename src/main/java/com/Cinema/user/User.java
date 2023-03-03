package com.Cinema.user;

import com.Cinema.user.userRole.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "account",
      uniqueConstraints = {
            @UniqueConstraint(columnNames = {"email", "phoneNumber"})
      })
public class User implements UserDetails {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @Column(name = "email")
   @NotBlank
   @Email
   private String email;

   @Column(name = "firstName")
   @NotBlank
   private String firstName;

   @Column(name = "lastName")
   @NotBlank
   private String lastName;

   @Column(name = "birthDate")
   @NotNull
   private LocalDate bDate;

   @Column(name = "phoneNumber", length = 11)
   @Size(min = 9, max = 9)
   private String phoneNumber;

   @Column(name = "password")
   @NotBlank
   @Size(min = 6)
   private String password;

   @Column(name = "role")
   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(name = "account_roles",
         joinColumns = @JoinColumn(name = "account_id"),
         inverseJoinColumns = @JoinColumn(name = "role_id"))
   private Set<UserRole> roles = new HashSet<>();

   public User(Long id, String email, String firstName, String lastName, LocalDate bDate, String phoneNumber, String password, Set<UserRole> roles) {
      this.id = id;
      this.email = email;
      this.firstName = firstName;
      this.lastName = lastName;
      this.bDate = bDate;
      this.phoneNumber = phoneNumber;
      this.password = password;
      this.roles = roles;
   }

   public User() {

   }

   public Long getId() {
      return id;
   }

   public String getEmail() {
      return email;
   }

   public String getFirstName() {
      return firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public LocalDate getbDate() {
      return bDate;
   }

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public String getPassword() {
      return password;
   }

   public Set<UserRole> getRoles() {
      return roles;
   }

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
      return true;
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
