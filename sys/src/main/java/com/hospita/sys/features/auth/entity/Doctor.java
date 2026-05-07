package com.hospita.sys.features.auth.entity;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Doctor {
    
    @Id
    private Long id;
    private String bio;
    private float rating;
    private String specialization;



    @ElementCollection
    private List<String> certificates; // URLs

    // @OneToMany(mappedBy = "doctor")
    // private List<Appointment> appointments = new ArrayList<>();

    @OneToOne
    private User user;

}
