package com.hospita.sys.features.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Entity
@Getter
public class Docktor {
    
    @Id
    private long id;
    private String bio;
    private float rating;
    private String specialization;

    @OneToOne
    private User user;

}
