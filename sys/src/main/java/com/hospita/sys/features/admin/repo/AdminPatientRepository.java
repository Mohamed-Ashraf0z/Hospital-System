package com.hospita.sys.features.admin.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospita.sys.features.auth.entity.User;
import com.hospita.sys.features.patient.entity.Patient;

@Repository
public interface AdminPatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser(User user);
}
