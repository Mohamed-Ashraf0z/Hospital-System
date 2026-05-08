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

    // @OneToMany(mappedBy = "patient")
    // private List<Appointment> appointments = new ArrayList<>();

    @OneToOne
    private User user;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public historyD getHistory() { return history; }
    public void setHistory(historyD history) { this.history = history; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
