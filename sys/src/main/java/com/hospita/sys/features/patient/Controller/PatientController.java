package com.hospita.sys.features.patient.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospita.sys.features.auth.entity.ApiResponse;
import com.hospita.sys.features.patient.entity.historyD;
import com.hospita.sys.features.patient.service.HistoryService;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private HistoryService historyService;

    @GetMapping
    public ResponseEntity<ApiResponse> getDoctors() {
        return null;
    }

    @GetMapping("/doctors/{id}")
    public ResponseEntity<ApiResponse> getDoctor(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<ApiResponse> getHistory(@PathVariable long id) {
        return historyService.getHistory(id);
    }

    @GetMapping("/appointments")
    public ResponseEntity<ApiResponse> getDoctorAppointments() {
        return null;
    }

    @PostMapping("/appointment")
    public ResponseEntity<ApiResponse> makeAppointment() {
        return null;
    }

    @PostMapping("/cancel-appointment")
    public ResponseEntity<ApiResponse> cancelAppointment() {
        return null;
    }

    @PostMapping("/rate-doctor")
    public ResponseEntity<ApiResponse> rateDoctor() {
        return null;
    }

    @PostMapping("/make-history")
    public ResponseEntity<ApiResponse> makeHistory(@RequestBody historyD historyD) {
        return historyService.makeHistory(historyD.getHistory(), historyD.getId());
    }


}