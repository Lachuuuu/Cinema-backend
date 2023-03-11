package com.Cinema.user;

import com.Cinema.user.userRole.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "account")
public class User implements UserDetails {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @Column(name = "email", unique = true)
   private String email;

   @Column(name = "firstName")
   private String firstName;

   @Column(name = "lastName")
   private String lastName;

   @Column(name = "birthDate")
   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
   private LocalDate bDate;

   @Column(name = "phoneNumber", length = 9, unique = true)
   private String phoneNumber;

   @Column(name = "password")
   @Size(min = 6, message = "haslo nie jest poprawne powinno miec przynajmniej 6 znakow")
   private String password;

   @Column(name = "role")
   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(name = "account_roles",
         joinColumns = @JoinColumn(name = "account_id"),
         inverseJoinColumns = @JoinColumn(name = "role_id"))
   private Set<UserRole> roles = new HashSet<>();

   @Column(name = "isActive")
   private boolean isActive = false;

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

   public boolean isActive() {
      return isActive;
   }

   public void setActive(boolean active) {
      isActive = active;
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
