package com.example.rqchallenge.utility;

import com.example.rqchallenge.entity.Employee;

import java.util.Map;
import java.util.UUID;

public class EmployeeUtility {

    public static Employee createEmployee(Map<String, Object> employeeInput) {
        String name = (String) employeeInput.get("name");
        String salary = (String) employeeInput.get("salary");
        String age = (String) employeeInput.get("age");


        // Parse salary and age strings to integers
        int parsedSalary = Integer.parseInt(salary);
        int parsedAge = Integer.parseInt(age);

        // Generate a unique ID for the employee
        String id = UUID.randomUUID().toString();
        Employee e = new Employee();
        e.setEmployee_name(name);
        e.setId(id);
        e.setEmployee_salary(parsedSalary);
        e.setEmployee_age(parsedAge);


        return e;
    }
}