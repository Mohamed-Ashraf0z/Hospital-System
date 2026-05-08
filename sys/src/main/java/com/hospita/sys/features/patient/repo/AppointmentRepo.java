package com.hospita.sys.features.patient.repo;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospita.sys.features.patient.entity.Appointment;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

    public Object findByPatient(long patient);
    Optional<Appointment> findByAvailabilitySlot(Long availabilitySlot);
    // Optional<User> findByEmail(String Email);
}
