package com.hospita.sys.features.doctor.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospita.sys.features.auth.entity.Doctor;
import com.hospita.sys.features.doctor.entity.PatientReport;

@Repository
public interface PatientReportRepository extends JpaRepository<PatientReport, Long> {
    List<PatientReport> findByDoctor(Doctor doctor);
    
    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.transaction.annotation.Transactional
    void deleteByDoctor(Doctor doctor);
    
    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("DELETE FROM PatientReport p WHERE p.patientUserId = :patientUserId")
    void deleteByPatientUserId(@org.springframework.data.repository.query.Param("patientUserId") Long patientUserId);
}
