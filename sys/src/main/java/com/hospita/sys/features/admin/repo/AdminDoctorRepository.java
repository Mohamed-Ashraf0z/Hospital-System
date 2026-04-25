package com.hospita.sys.features.admin.repo;

import com.hospita.sys.features.auth.entity.Doctor;
import com.hospita.sys.features.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminDoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser(User user);
}
