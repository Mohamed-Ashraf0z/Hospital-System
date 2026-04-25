package com.hospita.sys.features.admin.exception;

import org.springframework.http.HttpStatus;

public class AdminAccessDeniedException extends AdminException {
    public AdminAccessDeniedException() {
        super("Unauthorized: Admin access required or invalid token.", HttpStatus.UNAUTHORIZED);
    }
}
