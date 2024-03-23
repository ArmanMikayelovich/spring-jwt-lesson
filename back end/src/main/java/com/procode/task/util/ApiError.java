package com.procode.task.util;

import lombok.Getter;

@Getter
public class ApiError {

    private final String message;
    private final int status;

    public ApiError(String message, int statusCode) {
        this.message = message;
        this.status = statusCode;
    }
}