package com.hospita.sys.features.patient.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hospita.sys.features.auth.entity.ApiResponse;
import com.hospita.sys.features.auth.entity.Doctor;
import com.hospita.sys.features.auth.repo.AuthRepo;
import com.hospita.sys.features.auth.repo.DoctorRepository;
import com.hospita.sys.features.doctor.repo.AvailabilitySlotRepository;

@Service
public class PatientDoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AvailabilitySlotRepository availabilitySlotRepository;

    @Autowired
    private AuthRepo authRepo;

    public ResponseEntity<ApiResponse> getDoctors() {
        var doctors = doctorRepository.findAll();
        var doctorsnames = ((Collection<Doctor>) doctors).stream().map(doctor -> authRepo.findById(doctor.getId()).get().getUsername()).toList();
        List<Object> Doctorlist = new ArrayList<>();
        for (int i = 0; i < doctors.size(); i++) {
            Doctorlist.add(doctorsnames.get(i));
            Doctorlist.add(doctors.get(i));
        }
        return ResponseEntity.ok(new ApiResponse(true, "success",Doctorlist , null));
    }

    public ResponseEntity<ApiResponse> getDoctor(long id) {
        if (doctorRepository.findById(id).isEmpty()) {
            return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse(false, "not found", null, null));
        }
        
        List<Object> res = new ArrayList<>();
        res.add(authRepo.findById(id).get().getUsername());
        res.add(doctorRepository.findById(id).get());
        res.add(availabilitySlotRepository.findByDoctor(doctorRepository.findById(id).get()));
        return ResponseEntity.ok(new ApiResponse(true, "success", res , null));
    }

    // public ResponseEntity<ApiResponse> rateDoctor(long id) {
    //     if (doctorRepository.findById(id).isEmpty()) {
    //         return ResponseEntity
    //         .status(HttpStatus.NOT_FOUND)
    //         .body(new ApiResponse(false, "not found", null, null));
    //     }
    // }

}
