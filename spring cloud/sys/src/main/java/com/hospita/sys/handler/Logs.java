package com.hospita.sys.handler;

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
}
