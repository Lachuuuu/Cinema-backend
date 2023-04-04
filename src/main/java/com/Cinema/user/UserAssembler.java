package com.Cinema.user;

import com.Cinema.user.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserAssembler {
   public UserDto toDto(User user) {
      return new UserDto(user.getEmail(), user.getFirstName(), user.getLastName(), user.getBirthDate(), user.getPhoneNumber(), user.getRoles());
   }
}
