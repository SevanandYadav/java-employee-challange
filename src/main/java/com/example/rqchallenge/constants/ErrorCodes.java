package com.example.rqchallenge.constants;

import lombok.Getter;

@Getter
public enum ErrorCodes {
    EMPLOYEE_NOT_FOUND("10400", "EMPLOYEE NOT FOUND"),

    INVALID_EMPLOYEE_DETAIL("10401", "INVALID_EMPLOYEE_DETAIL"),
    TOO_FREQUENT_REQUESTS("10402", "TOO_FREQUENT_REQUESTS");

    private final String code;
    private final String msg;

    ErrorCodes(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
