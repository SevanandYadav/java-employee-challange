package com.example.rqchallenge.constants;

import lombok.Getter;

@Getter
public enum APIConstants {
    EMPLOYEE("employees"),
    EMPLOYEE_BY_ID("employee/"),
    BASE_URL("https://dummy.restapiexample.com/api/v1/"),
    CREATE_EMPLOYEE("create"),
    DELETE_EMPLOYEE("delete/");

    private final String value;

    APIConstants(String value) {
        this.value = value;
    }

}
