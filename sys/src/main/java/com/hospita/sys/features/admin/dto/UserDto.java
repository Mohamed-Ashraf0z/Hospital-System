package com.hospita.sys.features.admin.dto;

import com.hospita.sys.features.auth.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String username;
    private String role;
    private boolean isFrozen;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.isFrozen = user.isFrozen();
    }
}
