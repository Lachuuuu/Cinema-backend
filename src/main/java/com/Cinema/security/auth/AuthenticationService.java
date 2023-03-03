package com.Cinema.security.auth;

import com.Cinema.security.jwt.JwtService;
import com.Cinema.user.User;
import com.Cinema.user.UserRepository;
import com.Cinema.user.userRole.UserRole;
import com.Cinema.user.userRole.UserRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Transactional
public class AuthenticationService {

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private UserRoleRepository userRoleRepository;

   @Autowired
   private JwtService jwtService;

   @Autowired
   private AuthenticationManager authenticationManager;

   @Autowired
   private PasswordEncoder passwordEncoder;

   public AuthenticationResponse register(RegisterRequest request) {

      UserRole userRole = userRoleRepository.findByName("USER").orElse(null);

      User user = new User(null,
            request.getEmail(),
            request.getFirstName(),
            request.getLastName(),
            request.getbDate(),
            request.getPhoneNumber(),
            passwordEncoder.encode(request.getPassword()),
            Set.of(userRole));
      userRepository.save(user);

      var jwtToken = jwtService.generateToken(user);
      return new AuthenticationResponse(jwtToken);
   }

   public AuthenticationResponse authenticate(AuthenticationRequest request) {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
      ));

      User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

      var jwtToken = jwtService.generateToken(user);
      return new AuthenticationResponse(jwtToken);
   }

}
