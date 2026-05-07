package com.hospita.sys.features.patient.entity;

import java.time.LocalTime;

import com.hospita.sys.features.auth.entity.Doctor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** e.g. "MONDAY", "TUESDAY" */
    private String day;

    private LocalTime startTime;

    private LocalTime endTime;

    /** Duration of each appointment slot in minutes */
    private int durationMinutes;

    @ManyToOne
    private Doctor doctor;
}

