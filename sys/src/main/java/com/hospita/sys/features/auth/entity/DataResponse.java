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
}
