package com.hospita.sys.features.doctor.exception;

import org.springframework.http.HttpStatus;

public class AppointedPatientNotFoundException extends DoctorException {
    public AppointedPatientNotFoundException(Long id) {
        super("Appointed patient with id " + id + " not found.", HttpStatus.NOT_FOUND);
    }
}
