package com.Cinema.user.userRole;

import com.Cinema.user.userRole.dto.UserRoleDto;

public class UserRoleAssembler {

    public UserRoleDto toUserRoleDto(UserRole userRole) {
        return new UserRoleDto(userRole.getId(),
                userRole.getName()
        );
    }

}
