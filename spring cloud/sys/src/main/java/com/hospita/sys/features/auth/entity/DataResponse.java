package com.hospita.sys.features.auth.entity;

import lombok.Getter;

@Getter
public class DataResponse {
    private final boolean auth;
    private final String username;
    private final String role;
    private final Long id;

    public DataResponse(boolean auth, String username, String role,Long id) {
        this.auth = auth;
        this.username = username;
        this.role = role;
        this.id = id;
    }

    public boolean isAuth() { return auth; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public Long getId() { return id; }
}
