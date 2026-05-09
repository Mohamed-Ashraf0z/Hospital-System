package com.hospita.sys.features.auth.entity;

import lombok.Getter;

@Getter
public class ApiResponse {
    private final boolean success;
    private final String message;
    private final Object data;
    private final Object errors;

    public ApiResponse(boolean success, String message, Object data, Object errors) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    // getters & setters
}