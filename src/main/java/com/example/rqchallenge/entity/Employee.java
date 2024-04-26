package com.example.rqchallenge.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@JsonIgnoreProperties
@Data
public class Employee {

    private String id;
    private String employee_name;
    private int employee_salary;
    private int employee_age;
    private String profile_image;


}
