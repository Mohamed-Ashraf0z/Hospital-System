package com.hospita.sys.features.doctor.entity;

import java.time.LocalDateTime;

import com.hospita.sys.features.auth.entity.Doctor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "patient_reports")
@Getter
@Setter
@NoArgsConstructor
public class PatientReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The User id of the patient the report is addressed to */
    private Long patientUserId;

    private String patientName;

    @Column(length = 5000)
    private String reportContent;

    private LocalDateTime sentAt;

    @ManyToOne
    private Doctor doctor;
}
