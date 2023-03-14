package com.Cinema.user;

import com.Cinema.user.userRole.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "account",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email", "phoneNumber"})
        })
public class User {
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
    @Size(min = 11, max = 11)
    private String phoneNumber;

    @Column(name = "password")
    @NotBlank
    @Size(min = 6)
    private String password;

    @Column(name = "role")
    @OneToMany
    private List<UserRole> roles;

    public User(Long id, String email, String firstName, String lastName, LocalDate bDate, String phoneNumber, String password, List<UserRole> roles) {
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

    public List<UserRole> getRoles() {
        return roles;
    }
}
