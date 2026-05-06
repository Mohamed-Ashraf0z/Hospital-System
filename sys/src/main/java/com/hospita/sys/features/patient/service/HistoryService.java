package com.hospita.sys.features.patient.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hospita.sys.features.auth.entity.ApiResponse;
import com.hospita.sys.features.patient.entity.Patient;
import com.hospita.sys.features.patient.entity.historyD;
import com.hospita.sys.features.patient.repo.PatientRepository;
import com.hospita.sys.features.patient.repo.historyrepo;

@Service
public class HistoryService {
    
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private historyrepo historyrepo;

    public ResponseEntity<ApiResponse> getHistory(long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(
                    true, "success", patient.get().getHistory(),null));
        }
        // return ResponseEntity.notFound().build();
        return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ApiResponse(false, "not found", null, null));
    }

    public ResponseEntity<ApiResponse> makeHistory(String history, Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        
        if (!(patient.isPresent())) {
            return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ApiResponse(false, "not found", null, null));
        }
        historyD historyD1 = new historyD();
        historyD1.setId(id);
        historyD1.setHistory(history);
        historyrepo.save(historyD1);
        patient.get().setHistory(historyD1);
        return  ResponseEntity.ok(new ApiResponse(true, "success", patientRepository.save(patient.get()), null));
    }

}
