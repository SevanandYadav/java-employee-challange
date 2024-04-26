package com.example.rqchallenge.entity.response;

import lombok.Data;

@Data
public class EmployeeResponse<T> {
    private T data;
    private String message;
    private String status;

}