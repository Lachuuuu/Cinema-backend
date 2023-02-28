package com.Cinema.user;

import com.Cinema.user.dto.UserDto;

public class UserAssembler {

    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getbDate(),
                user.getPhoneNumber(),
                user.getRoles()
        );
    }
}
