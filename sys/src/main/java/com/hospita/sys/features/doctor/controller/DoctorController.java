package com.hospita.sys.features.doctor.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospita.sys.features.admin.annotation.RequiresPermission;
import com.hospita.sys.features.auth.entity.ApiResponse;
import com.hospita.sys.features.doctor.dto.AvailabilityRequest;
import com.hospita.sys.features.doctor.dto.BioUpdateRequest;
import com.hospita.sys.features.doctor.dto.BioUpdateResponse;
import com.hospita.sys.features.doctor.dto.ReportRequest;
import com.hospita.sys.features.doctor.entity.AppointedPatient;
import com.hospita.sys.features.doctor.entity.AvailabilitySlot;
import com.hospita.sys.features.doctor.entity.PatientReport;
import com.hospita.sys.features.doctor.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // ─────────────────────────────────────────────────────────────
    // 1. Update Bio  PUT /api/doctors/{id}/bio
    // ─────────────────────────────────────────────────────────────

    @PutMapping("/{id}/bio")
    @RequiresPermission("UPDATE_BIO")
    public ResponseEntity<ApiResponse> updateBio(
            @PathVariable Long id,
            @RequestBody BioUpdateRequest request) {

        BioUpdateResponse result = doctorService.updateBio(id, request);

        String message = result.isUpdated()
                ? "Bio updated successfully."
                : "No changes detected. Bio was not updated.";

        return ResponseEntity.ok(new ApiResponse(true, message, result, null));
    }

    // ─────────────────────────────────────────────────────────────
    // 2. Availability  POST /api/doctors/{id}/availability
    //                  GET  /api/doctors/{id}/availability
    // ─────────────────────────────────────────────────────────────

    /**
     * Submit one or more availability slots.
     * Body: JSON array of { day, startTime, endTime, durationMinutes }
     */
    @PostMapping("/{id}/availability")
    @RequiresPermission("ADD_AVAILABILITY")
    public ResponseEntity<ApiResponse> addAvailability(
            @PathVariable Long id,
            @RequestBody List<AvailabilityRequest> requests) {

        List<AvailabilitySlot> saved = doctorService.addAvailability(id, requests);
        return ResponseEntity.ok(new ApiResponse(true, "Availability slots saved.", saved, null));
    }

    /** Fetch all availability slots for the doctor (helper for the test UI). */
    @GetMapping("/{id}/availability")
    @RequiresPermission("GET_AVAILABILITY")
    public ResponseEntity<ApiResponse> getAvailability(@PathVariable Long id) {
        List<AvailabilitySlot> slots = doctorService.getAvailability(id);
        return ResponseEntity.ok(new ApiResponse(true, "Availability fetched.", slots, null));
    }

    // ─────────────────────────────────────────────────────────────
    // 3. Reports  GET  /api/doctors/{id}/appointed-patients
    //             POST /api/doctors/{id}/report
    // ─────────────────────────────────────────────────────────────

    /**
     * Returns the list of patients who attended an appointment with this doctor.
     * The frontend renders this as a dropdown from which the doctor picks a patient to report.
     */
    @GetMapping("/{id}/appointed-patients")
    @RequiresPermission("GET_APPOINTED_PATIENTS")
    public ResponseEntity<ApiResponse> getAppointedPatients(@PathVariable Long id) {
        List<AppointedPatient> patients = doctorService.getAppointedPatients(id);
        return ResponseEntity.ok(new ApiResponse(true, "Appointed patients fetched.", patients, null));
    }

    /**
     * Send a report to a specific patient.
     * Body: { appointedPatientId, reportContent }
     * After sending, the appointed-patient record is deleted.
     */
    @PostMapping("/{id}/report")
    @RequiresPermission("SEND_REPORT")
    public ResponseEntity<ApiResponse> sendReport(
            @PathVariable Long id,
            @RequestBody ReportRequest request) {

        PatientReport report = doctorService.sendReport(id, request);
        return ResponseEntity.ok(new ApiResponse(true, "Report sent to " + report.getPatientName() + ".", report, null));
    }
}
