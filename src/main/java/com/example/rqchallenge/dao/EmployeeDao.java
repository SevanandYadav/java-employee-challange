package com.example.rqchallenge.dao;

import com.example.rqchallenge.entity.Employee;
import com.example.rqchallenge.entity.response.EmployeeResponse;
import com.example.rqchallenge.utility.client.ApiExampleRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class EmployeeDao {
    private final ApiExampleRestClient apiExampleRestClient;

    @Autowired
    public EmployeeDao(ApiExampleRestClient apiExampleRestClient) {
        this.apiExampleRestClient = apiExampleRestClient;

    }

    public List<Employee> getEmployeeDetails() {

        Mono<EmployeeResponse<List<Employee>>> employeesResponseMono = apiExampleRestClient.getEmployees();
        EmployeeResponse<List<Employee>> employeesResponse = employeesResponseMono.block();
        if (employeesResponse == null || employeesResponse.getData() == null) {
            log.info("Null response or empty data");
            return Collections.EMPTY_LIST;
        }

        return employeesResponse.getData();

    }

    public ResponseEntity<Employee> getEmployeeById(String id) {


        Mono<EmployeeResponse<Employee>> employeesMOnoRes = apiExampleRestClient.getEmployeeById(id);
        EmployeeResponse<Employee> empRes = employeesMOnoRes.block();
        Employee employee = null;
        if (empRes != null)
            employee = empRes.getData();
        log.info("employees {} ", employee);
        if (employee == null) {
            log.info("null employees {} ", (Object) null);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(employee, HttpStatus.OK);

    }

    public Mono<EmployeeResponse<Employee>> createEmployees(Employee newEmployee) {

        return apiExampleRestClient.createEmployees(newEmployee);
    }
}
