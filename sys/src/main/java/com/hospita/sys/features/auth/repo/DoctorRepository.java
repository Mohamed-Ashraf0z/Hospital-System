package com.hospita.sys.features.auth.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospita.sys.features.auth.entity.Doctor;
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    // Optional<User> findByEmail(String Email);
}