package com.hospita.sys.features.patient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hospita.sys.features.auth.entity.ApiResponse;
import com.hospita.sys.features.auth.repo.DoctorRepository;

@Service
public class PatientDoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public ResponseEntity<ApiResponse> getDoctors() {
        return ResponseEntity.ok(new ApiResponse(true, "success", doctorRepository.findAll(), null));
    }

    public ResponseEntity<ApiResponse> getDoctor(long id) {
        if (doctorRepository.findById(id).isEmpty()) {
            return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse(false, "not found", null, null));
        }
        return ResponseEntity.ok(new ApiResponse(true, "success", doctorRepository.findById(id).get(), null));
    }

    // public ResponseEntity<ApiResponse> rateDoctor(long id) {
    //     if (doctorRepository.findById(id).isEmpty()) {
    //         return ResponseEntity
    //         .status(HttpStatus.NOT_FOUND)
    //         .body(new ApiResponse(false, "not found", null, null));
    //     }
    // }

}
