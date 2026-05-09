package com.hospita.sys.features.patient.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Appointment {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // @OneToOne
    @NotNull
    private Long availabilitySlot;
    @NotBlank(message = "Note is required")
    private String note;
    
    private String patientName;
    private String doctorName;
    private String history;
    
    // @ManyToOne
    // @JoinColumn(name = "patient_id")
    @NotNull
    private Long patient;

    // @ManyToOne
    // @JoinColumn(name = "doctor_id")
    @NotNull
    private Long doctor;
}
