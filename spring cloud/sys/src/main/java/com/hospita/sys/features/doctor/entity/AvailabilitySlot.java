package com.hospita.sys.features.doctor.entity;

import java.time.LocalTime;
import java.util.List;

import com.hospita.sys.features.auth.entity.Doctor;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "availability_slots")
@Getter
@Setter
@NoArgsConstructor
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "availability_slot_days", joinColumns = @JoinColumn(name = "availability_slot_id"))
    @Column(name = "day_name")
    private List<String> days;

    private LocalTime startTime;

    private LocalTime endTime;

    /** Duration of each appointment slot in minutes */
    private int durationMinutes;

    @ManyToOne
    private Doctor doctor;
}
