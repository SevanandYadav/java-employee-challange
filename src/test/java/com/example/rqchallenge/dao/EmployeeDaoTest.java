package com.example.rqchallenge.dao;

import com.example.rqchallenge.entity.Employee;
import com.example.rqchallenge.entity.response.EmployeeResponse;
import com.example.rqchallenge.utility.client.ApiExampleRestClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EmployeeDaoTest {
    @Mock
    private ApiExampleRestClient apiExampleRestClient;

    @InjectMocks
    private EmployeeDao employeeDao;


    @Test
    public void testGetEmployeeDetails_Success() {
        // Mock response from apiExampleRestClient
        List<Employee> expectedEmployees = getEmployeeList();
        EmployeeResponse<List<Employee>> response = new EmployeeResponse<>();
        response.setMessage(HttpStatus.OK.toString());
        response.setStatus("Success");
        response.setData(expectedEmployees);

        Mono<EmployeeResponse<List<Employee>>> responseMono = Mono.just(response);
        when(apiExampleRestClient.getEmployees()).thenReturn(responseMono);

        // Call the method being tested
        List<Employee> actualEmployees = employeeDao.getEmployeeDetails();

        // Verify that the method returns the expected list of employees
        assertEquals(expectedEmployees, actualEmployees);
    }

    @Test
    public void testGetEmployeeDetails_EmptyResponse() {
        // Mock empty response from apiExampleRestClient
        List<Employee> expectedEmployees = new ArrayList<>();
        EmployeeResponse<List<Employee>> response = new EmployeeResponse<>();
        response.setMessage(HttpStatus.OK.toString());
        response.setStatus("Success");
        response.setData(expectedEmployees);

        Mono<EmployeeResponse<List<Employee>>> responseMono = Mono.just(response);
        when(apiExampleRestClient.getEmployees()).thenReturn(responseMono);

        // Call the method being tested
        List<Employee> actualEmployees = employeeDao.getEmployeeDetails();

        // Verify that the method returns an empty list
        assertEquals(0, actualEmployees.size());
    }

    @Test
    public void testGetEmployeeById_Success() {
        // Mock response from apiExampleRestClient
        String employeeId = "1";
        Employee expectedEmployee = new Employee();
        expectedEmployee.setId(employeeId);
        expectedEmployee.setEmployee_name("John Doe");
        expectedEmployee.setEmployee_salary(50000);

        EmployeeResponse<Employee> response = new EmployeeResponse<>();
        response.setMessage(HttpStatus.OK.toString());
        response.setStatus("Success");
        response.setData(expectedEmployee);

        Mono<EmployeeResponse<Employee>> responseMono = Mono.just(response);
        when(apiExampleRestClient.getEmployeeById(employeeId)).thenReturn(responseMono);

        // Call the method being tested
        ResponseEntity<Employee> actualResponse = employeeDao.getEmployeeById(employeeId);

        // Verify that the method returns a ResponseEntity with the expected employee and HttpStatus.OK
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedEmployee, actualResponse.getBody());
    }

    @Test
    public void testGetEmployeeById_NullResponse() {
        // Mock null response from apiExampleRestClient
        String employeeId = "2";

        EmployeeResponse<Employee> response = new EmployeeResponse<>();
        response.setMessage(HttpStatus.OK.toString());
        response.setStatus("Success");
        response.setData(null);

        Mono<EmployeeResponse<Employee>> responseMono = Mono.just(response);
        when(apiExampleRestClient.getEmployeeById(employeeId)).thenReturn(responseMono);

        // Call the method being tested
        ResponseEntity<Employee> actualResponse = employeeDao.getEmployeeById(employeeId);

        // Verify that the method returns a ResponseEntity with HttpStatus.INTERNAL_SERVER_ERROR
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResponse.getStatusCode());
    }


    private static List<Employee> getEmployeeList() {
        Employee e1 = new Employee();
        e1.setEmployee_name("John");
        e1.setEmployee_salary(34344);
        Employee e2 = new Employee();
        e2.setEmployee_name("Alice");
        e2.setEmployee_salary(34300);

        return Arrays.asList(e1, e2);

    }
}
