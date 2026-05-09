package com.hospita.sys.features.doctor.entity;

import com.hospita.sys.features.auth.entity.Doctor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a patient that attended an appointment with a doctor.
 * The doctor can send a report to any patient in this list.
 * Once the report is sent the record is removed from this table.
 */
@Entity
@Table(name = "appointed_patients")
@Getter
@Setter
@NoArgsConstructor
public class AppointedPatient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The User id of the patient */
    private Long patientUserId;

    /** Display name shown in the doctor's dropdown list */
    private String patientName;

    @ManyToOne
    private Doctor doctor;
}
