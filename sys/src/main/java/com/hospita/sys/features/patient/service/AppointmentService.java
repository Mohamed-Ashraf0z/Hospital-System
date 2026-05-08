package com.hospita.sys.features.patient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hospita.sys.features.auth.entity.ApiResponse;
import com.hospita.sys.features.auth.repo.AuthRepo;
import com.hospita.sys.features.auth.repo.DoctorRepository;
import com.hospita.sys.features.doctor.entity.AppointedPatient;
import com.hospita.sys.features.doctor.repo.AppointedPatientRepository;
import com.hospita.sys.features.patient.entity.Appointment;
import com.hospita.sys.features.patient.repo.AppointmentRepo;
import com.hospita.sys.features.patient.repo.PatientRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepo appointmentRepo;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AuthRepo authRepo;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointedPatientRepository appointedPatientRepository;

    public ResponseEntity<ApiResponse> makeAppointment(Appointment appointment) {
        var patient = authRepo.findById(appointment.getPatient());
        var doctor = authRepo.findById(appointment.getDoctor());
        var appointmentch = appointmentRepo.findByAvailabilitySlot(appointment.getAvailabilitySlot());

        if (!(patient.isPresent() && doctor.isPresent())) {
            return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ApiResponse(false, "invalid user", null, null));
        }

        if (appointmentch.isPresent()) {
            return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ApiResponse(false, "this slot is taken", null, null));
        }

        appointment.setPatientName(authRepo.findById(appointment.getPatient()).get().getUsername());
        appointment.setDoctorName(authRepo.findById(appointment.getDoctor()).get().getUsername());

        var patientRecord = patientRepository.findById(appointment.getPatient()).orElse(null);
        String history = (patientRecord != null && patientRecord.getHistory() != null) ? patientRecord.getHistory().getHistory() : "";
        appointment.setHistory(history);
        appointmentRepo.save(appointment);

        // Create AppointedPatient record so the doctor can see and report this patient
        doctorRepository.findById(appointment.getDoctor()).ifPresent(doctorEntity -> {
            AppointedPatient appointedPatient = new AppointedPatient();
            appointedPatient.setPatientUserId(appointment.getPatient());
            appointedPatient.setPatientName(appointment.getPatientName());
            appointedPatient.setDoctor(doctorEntity);
            appointedPatientRepository.save(appointedPatient);
        });
        return ResponseEntity.ok(new ApiResponse(true, "success", appointment, null));
    }


    public ResponseEntity<ApiResponse> cancelAppointment(long slotid, long patientid) {
        if (!(appointmentRepo.findById(slotid).isPresent() && patientRepository.findById(patientid).isPresent())) {
            return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ApiResponse(false, "not found", null, null));
        }
        if (appointmentRepo.findById(slotid).get().getPatient().longValue() != patientid) {
            return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(new ApiResponse(false, "not your appointment", null, null));
        }
        appointmentRepo.deleteById(slotid);
        return ResponseEntity.ok(new ApiResponse(true, "success", null, null));
    }


    public ResponseEntity<ApiResponse> getMyAppointments(long id) {
        if (!patientRepository.findById(id).isPresent()) {
            return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ApiResponse(false, "not found", null, null));
        }
        return ResponseEntity.ok(new ApiResponse(true, "success", appointmentRepo.findByPatient(id), null));
    }
}