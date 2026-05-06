package com.hospita.sys.features.patient.entity;

import com.hospita.sys.features.auth.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Patient {
    @Id
    private Long id;
    @OneToOne
    private historyD history;

    @OneToOne
    private User user;
}
