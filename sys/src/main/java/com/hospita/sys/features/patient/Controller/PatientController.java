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
import com.hospita.sys.features.patient.Dto.CancelAppointmentDto;
import com.hospita.sys.features.patient.entity.Appointment;
import com.hospita.sys.features.patient.entity.historyD;
import com.hospita.sys.features.patient.service.AppointmentService;
import com.hospita.sys.features.patient.service.DoctorService;
import com.hospita.sys.features.patient.service.HistoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<ApiResponse> getDoctors() {
        return doctorService.getDoctors();
    }

    @GetMapping("/doctors/{id}")
    public ResponseEntity<ApiResponse> getDoctor(@PathVariable Long id) {
        return doctorService.getDoctor(id);
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<ApiResponse> getHistory(@PathVariable long id) {
        return historyService.getHistory(id);
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<ApiResponse> getMyAppointments(@PathVariable long id) {
        return appointmentService.getMyAppointments(id);
    }

    @PostMapping("/appointment")
    public ResponseEntity<ApiResponse> makeAppointment(@Valid @RequestBody Appointment appointment) {
        return appointmentService.makeAppointment(appointment);
    }

    @PostMapping("/cancel-appointment")
    public ResponseEntity<ApiResponse> cancelAppointment(@RequestBody CancelAppointmentDto cancelAppointmentDto) {
        return appointmentService.cancelAppointment(cancelAppointmentDto.getSlotid(), cancelAppointmentDto.getPatientid());
    }

    // @PostMapping("/rate-doctor")
    // public ResponseEntity<ApiResponse> rateDoctor(@RequestBody Long id) {
    //     return null;
    // }

    @PostMapping("/make-history")
    public ResponseEntity<ApiResponse> makeHistory(@RequestBody historyD historyD) {
        return historyService.makeHistory(historyD.getHistory(), historyD.getId());
    }


}