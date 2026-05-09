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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getHistory() { return history; }
    public void setHistory(String history) { this.history = history; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
}
