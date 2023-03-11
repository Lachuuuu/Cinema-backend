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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

   public User register(RegisterRequest request) throws BadRequestException {
      String validation = validateRegisterRequest(request);
      if (validation != null) throw new BadRequestException(validation);

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

      return user;
   }

   public String authenticate(AuthenticationRequest request) {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
      ));

      User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

      var jwtToken = jwtService.generateToken(user);
      return jwtToken;
   }

   private String validateRegisterRequest(RegisterRequest request) {
      if (!LocalDateTime.now().isBefore(request.getbDate().atStartOfDay())) return "wrong birth date";

      if (!request.getEmail().matches("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")) return "wrong email address";
      Boolean user = userRepository.existsByEmail(request.getEmail());
      if (user) return "email is taken";

      if (request.getPhoneNumber().length() != 9) return "wrong length of phone number";
      user = userRepository.existsByPhoneNumber(request.getPhoneNumber());
      if (user) return "phone number is taken";

      if (request.getPassword().length() < 4) return "too short password, password should have atleast 4 signs";

      return null;
   }

}
