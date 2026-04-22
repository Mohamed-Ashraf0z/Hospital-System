package com.hospita.sys.features.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Entity
@Getter
public class Patient {
    @Id
    private long id;
    // private String

    @OneToOne
    private User user;
}
