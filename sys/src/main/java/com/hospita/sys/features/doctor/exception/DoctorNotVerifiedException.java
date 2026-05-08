package com.hospita.sys.features.doctor.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a doctor tries to use a service but their account is not yet verified
 * (i.e. auth == false on the linked User record).
 */
public class DoctorNotVerifiedException extends DoctorException {
    public DoctorNotVerifiedException() {
        super("Account not verified. Please contact Admin.", HttpStatus.UNAUTHORIZED);
    }
}
