package com.hospita.sys.features.doctor.exception;

import org.springframework.http.HttpStatus;

public class DoctorNotFoundException extends DoctorException {
    public DoctorNotFoundException(Long id) {
        super("Doctor with id " + id + " not found.", HttpStatus.NOT_FOUND);
    }
}
