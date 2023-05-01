package com.Cinema.user;

import com.Cinema.security.auth.exception.BadRequestException;
import com.Cinema.security.auth.request.RegisterRequest;
import com.Cinema.security.jwt.JwtService;
import com.Cinema.user.request.UpdateEmailRequest;
import com.Cinema.user.request.UpdateNameRequest;
import com.Cinema.user.request.UpdatePasswordRequest;
import com.Cinema.user.request.UpdatePhoneNumberRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

   private final UserRepository userRepository;

   private final JwtService jwtService;

   private final Validator validator;

   private final PasswordEncoder passwordEncoder;

   public User getUserByToken(String token) throws BadRequestException {
      User user = userRepository.findByEmail(jwtService.extractUserEmail(token)).orElse(null);
      if (user == null) throw new BadRequestException("User not found");
      return user;
   }

   public User changeEmail(User user, UpdateEmailRequest updateUserRequest) throws BadRequestException {
      final Set<ConstraintViolation<UpdateEmailRequest>> constraints = validator.validate(updateUserRequest);
      if (!constraints.isEmpty()) throw new BadRequestException(constraints.iterator().next().getMessage());

      if (!user.getEmail().equals(updateUserRequest.getOldValue())) throw new BadRequestException("old email is wrong");
      if (!checkIfEmailIsUnique(updateUserRequest.getNewValue())) throw new BadRequestException("new email is taken");

      user.setEmail(updateUserRequest.getNewValue());
      userRepository.save(user);
      return user;
   }

   public User changePassword(User user, UpdatePasswordRequest updatePasswordRequest) throws BadRequestException {
      final Set<ConstraintViolation<UpdatePasswordRequest>> constraints = validator.validate(updatePasswordRequest);
      if (!constraints.isEmpty()) throw new BadRequestException(constraints.iterator().next().getMessage());

      if (!passwordEncoder.matches(updatePasswordRequest.getOldValue(), user.getPassword()))
         throw new BadRequestException("old password is wrong");

      user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewValue()));
      userRepository.save(user);
      return user;
   }

   public User changeFirstName(User user, UpdateNameRequest updateNameRequest) throws BadRequestException {
      final Set<ConstraintViolation<UpdateNameRequest>> constraints = validator.validate(updateNameRequest);
      if (!constraints.isEmpty()) throw new BadRequestException(constraints.iterator().next().getMessage());

      if (!user.getFirstName().equals(updateNameRequest.getOldValue()))
         throw new BadRequestException("old first name is wrong");

      user.setFirstName(updateNameRequest.getNewValue());
      userRepository.save(user);
      return user;
   }

   public User changeLastName(User user, UpdateNameRequest updateNameRequest) throws BadRequestException {
      final Set<ConstraintViolation<UpdateNameRequest>> constraints = validator.validate(updateNameRequest);
      if (!constraints.isEmpty()) throw new BadRequestException(constraints.iterator().next().getMessage());

      if (!user.getLastName().equals(updateNameRequest.getOldValue()))
         throw new BadRequestException("old last name is wrong");

      user.setLastName(updateNameRequest.getNewValue());
      userRepository.save(user);
      return user;
   }

   public User changePhoneNumber(User user, UpdatePhoneNumberRequest updatePhoneNumberRequest) throws BadRequestException {
      final Set<ConstraintViolation<UpdatePhoneNumberRequest>> constraints = validator.validate(updatePhoneNumberRequest);
      if (!constraints.isEmpty()) throw new BadRequestException(constraints.iterator().next().getMessage());

      if (!user.getPhoneNumber().equals(updatePhoneNumberRequest.getOldValue()))
         throw new BadRequestException("old phone number is wrong");
      if (!checkIfPhoneNumberIsUnique(updatePhoneNumberRequest.getNewValue()))
         throw new BadRequestException("phone number is already taken");

      user.setPhoneNumber(updatePhoneNumberRequest.getNewValue());
      userRepository.save(user);
      return user;
   }

   public User deactivateAccount(User user) {
      user.setActive(false);
      return userRepository.save(user);
   }

   public String checkIfRequestIsUnique(RegisterRequest request) {

      if (!checkIfEmailIsUnique(request.getEmail())) return "email is taken";
      if (!checkIfPhoneNumberIsUnique(request.getPhoneNumber())) return "phone number is taken";

      return null;
   }

   private Boolean checkIfEmailIsUnique(String email) {
      return !userRepository.existsByEmail(email);
   }

   private Boolean checkIfPhoneNumberIsUnique(String phoneNumber) {
      return !userRepository.existsByPhoneNumber(phoneNumber);
   }

}
