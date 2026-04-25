package com.hospita.sys.features.admin.dto;

import com.hospita.sys.features.auth.entity.Doctor;
import com.hospita.sys.features.auth.entity.User;

import lombok.Data;

@Data
public class DoctorPendingDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    
    // Doctor profile info
    private String specialization;
    private String bio;

    public DoctorPendingDto(User user, Doctor doctor) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        
        if (doctor != null) {
            this.specialization = doctor.getSpecialization();
            this.bio = doctor.getBio();
        }
    }
}
