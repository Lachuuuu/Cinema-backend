package com.Cinema.user;

import com.Cinema.user.userRole.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
   @NotBlank(message = "email nie jest poprawny")
   @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
         flags = Pattern.Flag.CASE_INSENSITIVE, message = "email nie jest poprawny")
   private String email;

   @Column(name = "firstName")
   @NotBlank(message = "imie nie jest poprawne")
   private String firstName;

   @Column(name = "lastName")
   @NotBlank(message = "nazwisko nie jest poprawne")
   private String lastName;

   @Column(name = "birthDate")
   @NotNull(message = "data urodzenia nie jest poprawna")
   private LocalDate bDate;

   @Column(name = "phoneNumber", length = 9, unique = true)
   @Size(min = 9, max = 9, message = "bledny numer telefonu")
   private String phoneNumber;

   @Column(name = "password")
   @NotBlank(message = "haslo nie jest poprawne")
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
