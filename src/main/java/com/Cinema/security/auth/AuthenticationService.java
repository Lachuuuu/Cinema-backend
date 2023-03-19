package com.Cinema.security.auth;

import com.Cinema.security.auth.exception.BadRequestException;
import com.Cinema.security.auth.request.AuthenticationRequest;
import com.Cinema.security.auth.request.RegisterRequest;
import com.Cinema.security.jwt.JwtService;
import com.Cinema.user.User;
import com.Cinema.user.UserRepository;
import com.Cinema.user.userRole.UserRole;
import com.Cinema.user.userRole.UserRoleRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {

   private final UserRepository userRepository;

   private final UserRoleRepository userRoleRepository;

   private final JwtService jwtService;

   private final AuthenticationManager authenticationManager;

   private final PasswordEncoder passwordEncoder;

   private final Validator validator;

   public User register(RegisterRequest request) throws BadRequestException {
      final UserRole userRole = userRoleRepository.findByName("USER").orElse(null);
      final User user = new User(null,
            request.getEmail(),
            request.getFirstName(),
            request.getLastName(),
            request.getBirthDate(),
            request.getPhoneNumber(),
            passwordEncoder.encode(request.getPassword()),
            Set.of(userRole),
            false);

      final String unique = checkIfRequestIsUnique(request);
      if (unique != null) throw new BadRequestException(unique);

      final Set<ConstraintViolation<User>> constraints = validator.validate(user);
      if (!constraints.isEmpty()) throw new BadRequestException(constraints.iterator().next().getMessage());
      final Set<ConstraintViolation<RegisterRequest>> constraintsDTO = validator.validate(request);
      if (!constraintsDTO.isEmpty()) throw new BadRequestException(constraintsDTO.iterator().next().getMessage());

      userRepository.save(user);
      return user;
   }

   public String authenticate(AuthenticationRequest request) {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
      ));

      final User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

      return jwtService.generateToken(user);
   }

   private String checkIfRequestIsUnique(RegisterRequest request) {

      if (userRepository.existsByEmail(request.getEmail())) return "email is taken";
      if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) return "phone number is taken";

      return null;
   }

}
