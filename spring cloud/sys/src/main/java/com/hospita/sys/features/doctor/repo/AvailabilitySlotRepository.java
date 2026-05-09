package com.hospita.sys.features.doctor.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospita.sys.features.auth.entity.Doctor;
import com.hospita.sys.features.doctor.entity.AvailabilitySlot;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {
    List<AvailabilitySlot> findByDoctor(Doctor doctor);
    
    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.transaction.annotation.Transactional
    void deleteByDoctor(Doctor doctor);
}
