package com.hospita.sys.features.doctor.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hospita.sys.features.auth.entity.Doctor;
import com.hospita.sys.features.auth.entity.User;
import com.hospita.sys.features.auth.repo.AuthRepo;
import com.hospita.sys.features.auth.repo.DoctorRepository;
import com.hospita.sys.features.doctor.dto.AvailabilityRequest;
import com.hospita.sys.features.doctor.dto.BioUpdateRequest;
import com.hospita.sys.features.doctor.dto.BioUpdateResponse;
import com.hospita.sys.features.doctor.dto.ReportRequest;
import com.hospita.sys.features.doctor.entity.AppointedPatient;
import com.hospita.sys.features.doctor.entity.AvailabilitySlot;
import com.hospita.sys.features.doctor.entity.PatientReport;
import com.hospita.sys.features.doctor.exception.AppointedPatientNotFoundException;
import com.hospita.sys.features.doctor.exception.DoctorNotFoundException;
import com.hospita.sys.features.doctor.exception.DoctorNotVerifiedException;
import com.hospita.sys.features.doctor.repo.AppointedPatientRepository;
import com.hospita.sys.features.doctor.repo.AvailabilitySlotRepository;
import com.hospita.sys.features.doctor.repo.PatientReportRepository;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;
    private final AppointedPatientRepository appointedPatientRepository;
    private final PatientReportRepository patientReportRepository;
    private final AuthRepo userRepository;

    public DoctorService(
            DoctorRepository doctorRepository,
            AvailabilitySlotRepository availabilitySlotRepository,
            AppointedPatientRepository appointedPatientRepository,
            PatientReportRepository patientReportRepository,
            AuthRepo userRepository) {
        this.doctorRepository = doctorRepository;
        this.availabilitySlotRepository = availabilitySlotRepository;
        this.appointedPatientRepository = appointedPatientRepository;
        this.patientReportRepository = patientReportRepository;
        this.userRepository = userRepository;
    }

    // ─────────────────────────────────────────────────────────────
    // Helper: load doctor and verify auth
    // ─────────────────────────────────────────────────────────────

    private Doctor getVerifiedDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseGet(() -> {
                    // Self-healing: if User exists as a verified Doctor, create the profile
                    User user = userRepository.findById(doctorId).orElse(null);
                    if (user != null && "DOCTOR".equalsIgnoreCase(user.getRole()) && user.getAuth()) {
                        Doctor newDoctor = new Doctor();
                        newDoctor.setId(user.getId());
                        newDoctor.setUser(user);
                        return doctorRepository.save(newDoctor);
                    }
                    throw new DoctorNotFoundException(doctorId);
                });
        
        // Ensure the doctor is verified
        if (doctor.getUser() == null || !doctor.getUser().getAuth()) {
            throw new DoctorNotVerifiedException();
        }
        return doctor;
    }

    // ─────────────────────────────────────────────────────────────
    // 1. Update Bio
    // ─────────────────────────────────────────────────────────────

    @Transactional
    public BioUpdateResponse updateBio(Long doctorId, BioUpdateRequest request) {
        Doctor doctor = getVerifiedDoctor(doctorId);

        String oldBio = doctor.getBio();
        String newBio = request.getBio();

        // Trim to avoid whitespace-only "changes"
        boolean hasChanged = newBio != null && !newBio.trim().equals(oldBio == null ? "" : oldBio.trim());

        if (!hasChanged) {
            // Nothing changed – return current bio without hitting the DB
            return new BioUpdateResponse(oldBio, oldBio, false);
        }

        doctor.setBio(newBio.trim());
        doctorRepository.save(doctor);

        return new BioUpdateResponse(oldBio, newBio.trim(), true);
    }

    // ─────────────────────────────────────────────────────────────
    // 2. Add Availability Slots
    // ─────────────────────────────────────────────────────────────

    @Transactional
    public List<AvailabilitySlot> addAvailability(Long doctorId, List<AvailabilityRequest> requests) {
        Doctor doctor = getVerifiedDoctor(doctorId);

        List<AvailabilitySlot> slots = requests.stream().map(req -> {
            AvailabilitySlot slot = new AvailabilitySlot();
            slot.setDays(req.getDays().stream().map(String::toUpperCase).toList());
            slot.setStartTime(LocalTime.parse(req.getStartTime()));
            slot.setEndTime(LocalTime.parse(req.getEndTime()));
            slot.setDurationMinutes(req.getDurationMinutes());
            slot.setDoctor(doctor);
            return slot;
        }).toList();

        return availabilitySlotRepository.saveAll(slots);
    }

    /** Returns all existing slots for the given doctor (used by the frontend to show current schedule). */
    public List<AvailabilitySlot> getAvailability(Long doctorId) {
        Doctor doctor = getVerifiedDoctor(doctorId);
        return availabilitySlotRepository.findByDoctor(doctor);
    }

    // ─────────────────────────────────────────────────────────────
    // 3. Patient Report
    // ─────────────────────────────────────────────────────────────

    /** Returns the list of patients the doctor can send reports to. */
    public List<AppointedPatient> getAppointedPatients(Long doctorId) {
        Doctor doctor = getVerifiedDoctor(doctorId);
        return appointedPatientRepository.findByDoctor(doctor);
    }

    @Transactional
    public PatientReport sendReport(Long doctorId, ReportRequest request) {
        Doctor doctor = getVerifiedDoctor(doctorId);

        AppointedPatient appointedPatient = appointedPatientRepository.findById(request.getAppointedPatientId())
                .orElseThrow(() -> new AppointedPatientNotFoundException(request.getAppointedPatientId()));

        // Build and persist the report
        PatientReport report = new PatientReport();
        report.setDoctor(doctor);
        report.setPatientUserId(appointedPatient.getPatientUserId());
        report.setPatientName(appointedPatient.getPatientName());
        report.setReportContent(request.getReportContent());
        report.setSentAt(LocalDateTime.now());
        patientReportRepository.save(report);

        // Remove patient from the appointed list now that the report is sent
        appointedPatientRepository.delete(appointedPatient);

        return report;
    }
}
