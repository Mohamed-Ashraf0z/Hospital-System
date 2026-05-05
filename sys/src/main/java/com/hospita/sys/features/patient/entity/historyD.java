package com.hospita.sys.features.patient.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class historyD {
    @Id
    private Long id;
    private String history;

    @OneToOne
    private Patient patient;
}
