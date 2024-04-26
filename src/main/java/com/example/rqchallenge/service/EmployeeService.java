package com.example.rqchallenge.service;

import com.example.rqchallenge.constants.ErrorCodes;
import com.example.rqchallenge.dao.EmployeeDao;
import com.example.rqchallenge.entity.Employee;
import com.example.rqchallenge.entity.response.EmployeeDeleteResponse;
import com.example.rqchallenge.entity.response.EmployeeResponse;
import com.example.rqchallenge.exception.TooFrequentRequestException;
import com.example.rqchallenge.utility.client.ApiExampleRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeService {
    private final ApiExampleRestClient apiExampleRestClient;

    private final EmployeeDao employeeDao;

    @Autowired
    public EmployeeService(ApiExampleRestClient apiExampleRestClient, EmployeeDao employeeDao) {
        this.apiExampleRestClient = apiExampleRestClient;
        this.employeeDao = employeeDao;

    }

    public ResponseEntity<List<Employee>> getEmployeeDetails() {
        try {
            List<Employee> employees = employeeDao.getEmployeeDetails();
            log.debug("getEmployeeDetails: {}", employees);

            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            handleTooFrequentAccess(e);
            log.error("getEmployeeDetails Error: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


    public Mono<EmployeeDeleteResponse> deleteEmployeeById(String deleteId) throws TooFrequentRequestException {
        return apiExampleRestClient.deleteEmployeeById(deleteId)
                .onErrorMap(WebClientResponseException.class, e -> {
                    if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                        throw new TooFrequentRequestException(ErrorCodes.TOO_FREQUENT_REQUESTS.getCode() + ":" + ErrorCodes.TOO_FREQUENT_REQUESTS.getMsg());
                    } else {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete employee", e);
                    }
                });
    }


    public ResponseEntity<List<Employee>> getEmployeeByName(String searchName) {
        try {
            List<Employee> employees = employeeDao.getEmployeeDetails();
            log.debug("Employees: {}", employees);

            List<Employee> specificEmployee = employees.stream().filter(e -> e.getEmployee_name().equalsIgnoreCase(searchName)).collect(Collectors.toList());
            if (specificEmployee.isEmpty()) {

                return new ResponseEntity<>(specificEmployee, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(specificEmployee, HttpStatus.OK);
        } catch (Exception e) {
            handleTooFrequentAccess(e);
            log.error("Error: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Employee> getEmployeeById(String id) {
        try {
            return employeeDao.getEmployeeById(id);
        } catch (Exception e) {
            handleTooFrequentAccess(e);
            log.error("Error {} ", e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        try {
            List<Employee> employees = employeeDao.getEmployeeDetails();
            log.debug("getHighestSalaryOfEmployees employees {} ", employees);
            if (employees == null) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            OptionalInt maxSal = employees.stream()
                    .mapToInt(Employee::getEmployee_salary)
                    .max();

            return new ResponseEntity<>(maxSal.isPresent() ? maxSal.getAsInt() : 0, HttpStatus.OK);
        } catch (Exception e) {
            handleTooFrequentAccess(e);
            log.error("getHighestSalaryOfEmployees Error {} ", e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        try {
            List<Employee> employees = employeeDao.getEmployeeDetails();
            log.debug("employees {} ", employees);
            if (employees == null) {
                log.warn("null employees getTopTenHighestEarningEmployeeNames {} ", (Object) null);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            List<String> employeesHighestPAying = employees.stream()
                    .sorted(Comparator.comparingInt(Employee::getEmployee_salary).reversed())
                    .limit(10)
                    .map(Employee::getEmployee_name)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(employeesHighestPAying, HttpStatus.OK);
        } catch (Exception e) {
            handleTooFrequentAccess(e);
            log.error("getTopTenHighestEarningEmployeeNames Error {} ", e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Employee> createEmployeeRecord(Employee newEmployee) {

        try {
            Mono<EmployeeResponse<Employee>> employeesMono = employeeDao.createEmployees(newEmployee);
            Employee employee = employeesMono.block().getData();
            log.debug(" created employees {} ", employee);
            if (employee == null) {
                log.warn("null employees createEmployeeRecord{} ", (Object) null);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(employee);
        } catch (Exception e) {
            handleTooFrequentAccess(e);
            log.error("createEmployeeRecord Error {} ", e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    void handleTooFrequentAccess(Exception e) {
        if (e instanceof WebClientResponseException) {
            WebClientResponseException webClientError = (WebClientResponseException) e;
            if (webClientError.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                throw new TooFrequentRequestException(ErrorCodes.TOO_FREQUENT_REQUESTS.getCode() + ":" + ErrorCodes.TOO_FREQUENT_REQUESTS.getMsg());
            }
        }
    }
}
