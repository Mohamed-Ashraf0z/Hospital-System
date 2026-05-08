package com.hospita.sys.features.doctor.exception;

import org.springframework.http.HttpStatus;

/**
 * Base exception for the Doctor feature.
 * Mirrors the pattern used in the admin feature.
 */
public abstract class DoctorException extends RuntimeException {
    private final HttpStatus status;

    public DoctorException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
