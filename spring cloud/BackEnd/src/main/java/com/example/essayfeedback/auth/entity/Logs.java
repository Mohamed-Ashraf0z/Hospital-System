package com.example.essayfeedback.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Logs {
    @Id
    private long id;
    private String essay;
    // Manual Getters
    public long getId() {
        return id;
    }

    public String getEssay() {
        return essay;
    }

    // Manual Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setEssay(String essay) {
        this.essay = essay;
    }


}
