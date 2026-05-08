package com.hospita.sys.features.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hospita.sys.features.admin.dto.DoctorPendingDto;
import com.hospita.sys.features.admin.dto.DoctorVerificationDecisionDto;
import com.hospita.sys.features.admin.exception.DoctorNotFoundException;
import com.hospita.sys.features.admin.repo.AdminDoctorRepository;
import com.hospita.sys.features.admin.repo.AdminUserRepository;
import com.hospita.sys.features.auth.entity.Doctor;
import com.hospita.sys.features.auth.entity.User;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorVerificationService {

    private final AdminUserRepository userRepository;
    private final AdminDoctorRepository doctorRepository;
    private final AdminService adminService;

    public DoctorVerificationService(AdminUserRepository userRepository,AdminDoctorRepository doctorRepository,AdminService adminService) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.adminService = adminService;
    }

    public List<DoctorPendingDto> getPendingDoctors() {
        return userRepository.findByRoleIgnoreCaseAndAuth("DOCTOR", false)
                .stream()
                .map(user -> {
                    Doctor doctor = doctorRepository.findByUser(user).orElse(null);
                    return new DoctorPendingDto(user, doctor);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public String verifyDoctor(Long id, DoctorVerificationDecisionDto decision) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));

        if (decision.getIsAccepted()) {
            user.authtrue(); // Method exists in User.java

            // Assign standard doctor permissions so all /api/doctors endpoints work
            user.getPermissions().addAll(java.util.Set.of(
                "UPDATE_BIO",
                "ADD_AVAILABILITY",
                "GET_AVAILABILITY",
                "GET_APPOINTED_PATIENTS",
                "SEND_REPORT",
                "GET_HISTORY"
            ));
            userRepository.save(user);

            // Ensure Doctor entity exists
            if (doctorRepository.findByUser(user).isEmpty()) {
                Doctor newDoctor = new Doctor();
                newDoctor.setId(user.getId());
                newDoctor.setUser(user);
                doctorRepository.save(newDoctor);
            }

            return "Doctor " + user.getUsername() + " has been accepted.";
        } else {
            adminService.deleteUser(id);
            return "Doctor " + user.getUsername() + " has been rejected and removed.";
        }
    }
}
