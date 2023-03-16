package com.Cinema.user;

import com.Cinema.user.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserAssembler {
   public UserDto toUserDto(User user) {
      return new UserDto(user.getEmail(), user.getFirstName(), user.getLastName(), user.getBDate(), user.getPhoneNumber(), user.getRoles());
   }
}
