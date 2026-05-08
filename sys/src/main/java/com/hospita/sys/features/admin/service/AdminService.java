package com.hospita.sys.features.admin.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hospita.sys.features.admin.dto.UserDto;
import com.hospita.sys.features.admin.exception.AdminUserNotFoundException;
import com.hospita.sys.features.admin.repo.AdminDoctorRepository;
import com.hospita.sys.features.admin.repo.AdminPatientRepository;
import com.hospita.sys.features.admin.repo.AdminUserRepository;
import com.hospita.sys.features.auth.entity.Doctor;
import com.hospita.sys.features.auth.entity.User;
import com.hospita.sys.features.doctor.repo.AppointedPatientRepository;
import com.hospita.sys.features.doctor.repo.AvailabilitySlotRepository;
import com.hospita.sys.features.doctor.repo.PatientReportRepository;
import com.hospita.sys.features.patient.entity.Patient;

@Service
public class AdminService {

    private final AdminUserRepository userRepository;
    private final AdminDoctorRepository doctorRepository;
    private final AdminPatientRepository patientRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;
    private final AppointedPatientRepository appointedPatientRepository;
    private final PatientReportRepository patientReportRepository;

    public AdminService(
            AdminUserRepository userRepository,
            AdminDoctorRepository doctorRepository,
            AdminPatientRepository patientRepository,
            AvailabilitySlotRepository availabilitySlotRepository,
            AppointedPatientRepository appointedPatientRepository,
            PatientReportRepository patientReportRepository) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.availabilitySlotRepository = availabilitySlotRepository;
        this.appointedPatientRepository = appointedPatientRepository;
        this.patientReportRepository = patientReportRepository;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(user -> !user.getRole().equalsIgnoreCase("Admin"))
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Long id) {
        System.out.println("Starting deletion for user ID: " + id);
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("User found: " + user.getUsername() + " (" + user.getRole() + ")");
            
            // 1. Cleanup as a Doctor
            Optional<Doctor> doctorOpt = doctorRepository.findByUser(user);
            if (doctorOpt.isEmpty()) {
                doctorOpt = doctorRepository.findById(id);
            }

            doctorOpt.ifPresent(doctor -> {
                System.out.println("Deleting doctor-specific data for ID: " + id);
                availabilitySlotRepository.deleteByDoctor(doctor);
                appointedPatientRepository.deleteByDoctor(doctor);
                patientReportRepository.deleteByDoctor(doctor);
                doctorRepository.delete(doctor);
                System.out.println("Doctor profile deleted.");
            });

            // 2. Cleanup as a Patient (Data linked to this user's ID)
            System.out.println("Deleting patient-linked data for ID: " + id);
            appointedPatientRepository.deleteByPatientUserId(id);
            patientReportRepository.deleteByPatientUserId(id);

            // 3. Cleanup Patient Profile
            Optional<Patient> patientOpt = patientRepository.findByUser(user);
            if (patientOpt.isEmpty()) {
                patientOpt = patientRepository.findById(id);
            }
            patientOpt.ifPresent(patient -> {
                patientRepository.delete(patient);
                System.out.println("Patient profile deleted.");
            });

            // 4. Finally delete the user entity
            userRepository.delete(user);
            System.out.println("User entity deleted successfully.");
        } else {
            System.out.println("User not found for ID: " + id);
        }
    }

    @Transactional
    public UserDto freezeUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFrozen(true);
            userRepository.save(user);
            return new UserDto(user);
        }
        throw new AdminUserNotFoundException(id);
    }

    @Transactional
    public UserDto unfreezeUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFrozen(false);
            userRepository.save(user);
            return new UserDto(user);
        }
        throw new AdminUserNotFoundException(id);
    }
}
