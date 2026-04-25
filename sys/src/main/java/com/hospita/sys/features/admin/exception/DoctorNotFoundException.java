package com.hospita.sys.features.admin.exception;

import org.springframework.http.HttpStatus;

public class DoctorNotFoundException extends AdminException {
    public DoctorNotFoundException(Long id) {
        super("Error: Doctor with ID " + id + " does not exist in the verification system.", HttpStatus.NOT_FOUND);
    }
}
