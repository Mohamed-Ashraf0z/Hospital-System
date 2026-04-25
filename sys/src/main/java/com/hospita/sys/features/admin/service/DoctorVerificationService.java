package com.hospita.sys.features.admin.service;

import com.hospita.sys.features.admin.exception.DoctorNotFoundException;
import com.hospita.sys.features.admin.dto.DoctorPendingDto;
import com.hospita.sys.features.admin.dto.DoctorVerificationDecisionDto;
import com.hospita.sys.features.admin.repo.AdminDoctorRepository;
import com.hospita.sys.features.admin.repo.AdminUserRepository;
import com.hospita.sys.features.auth.entity.Doctor;
import com.hospita.sys.features.auth.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorVerificationService {

    private final AdminUserRepository userRepository;
    private final AdminDoctorRepository doctorRepository;

    public DoctorVerificationService(AdminUserRepository userRepository, AdminDoctorRepository doctorRepository) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
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

    public String verifyDoctor(Long id, DoctorVerificationDecisionDto decision) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));

        if (decision.getIsAccepted()) {
            user.authtrue(); // Method exists in User.java
            userRepository.save(user);
            return "Doctor " + user.getUsername() + " has been accepted.";
        } else {
            // Delete the Doctor entity first if it exists due to foreign key constraints
            doctorRepository.findByUser(user).ifPresent(doctorRepository::delete);
            // Delete the user if rejected so they don't stay in pending forever
            userRepository.delete(user);
            return "Doctor " + user.getUsername() + " has been rejected and removed.";
        }
    }
}
