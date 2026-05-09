package com.hospita.sys.features.admin.exception;

import org.springframework.http.HttpStatus;

public class AdminUserNotFoundException extends AdminException {
    public AdminUserNotFoundException(Long id) {
        super("Error: User with ID " + id + " does not exist.", HttpStatus.NOT_FOUND);
    }
}
