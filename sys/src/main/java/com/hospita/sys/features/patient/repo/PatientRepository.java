package com.hospita.sys.features.patient.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospita.sys.features.patient.entity.Patient;
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    // Optional<User> findByEmail(String Email);
}