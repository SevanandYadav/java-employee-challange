package com.example.rqchallenge.service;

import com.example.rqchallenge.dao.EmployeeDao;
import com.example.rqchallenge.entity.Employee;
import com.example.rqchallenge.entity.response.EmployeeDeleteResponse;
import com.example.rqchallenge.entity.response.EmployeeResponse;
import com.example.rqchallenge.utility.client.ApiExampleRestClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private ApiExampleRestClient apiExampleRestClient;

    @Mock
    private EmployeeDao employeeDao;

    @Test
    public void testGetEmployeeDetails() {
        List<Employee> employees = getEmployeeList();

        when(employeeDao.getEmployeeDetails()).thenReturn(employees);

        ResponseEntity<List<Employee>> responseEntity = employeeService.getEmployeeDetails();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(employees, responseEntity.getBody());
    }


    @Test
    public void testDeleteEmployeeById() {
        String deleteId = "1";
        EmployeeDeleteResponse expectedResponse = new EmployeeDeleteResponse();
        expectedResponse.setMessage("Employee deleted successfully.");
        expectedResponse.setStatus("SUCCESS");

        when(apiExampleRestClient.deleteEmployeeById(deleteId)).thenReturn(Mono.just(expectedResponse));

        Mono<EmployeeDeleteResponse> responseMono = employeeService.deleteEmployeeById(deleteId);
        if (responseMono != null) {
            EmployeeDeleteResponse employeeDeleteResponse = responseMono.block();
            if (employeeDeleteResponse != null) {
                assertEquals("SUCCESS", employeeDeleteResponse.getStatus());
            } else {

                throw new AssertionError("employeeDeleteResponse is null");
            }
        } else {

            throw new AssertionError("responseMono is null");
        }

    }

    @Test
    public void testGetEmployeeByName() {

        List<Employee> mockEmployees = getEmployeeList();

        when(employeeDao.getEmployeeDetails()).thenReturn(mockEmployees);

        String searchName = "John";

        ResponseEntity<List<Employee>> responseEntity = employeeService.getEmployeeByName(searchName);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<Employee> specificEmployees = responseEntity.getBody();

        List<String> expectedNames = Arrays.asList("John");
        List<String> actualNames = specificEmployees.stream().map(Employee::getEmployee_name).collect(Collectors.toList());
        assertEquals(expectedNames, actualNames);
    }

    @Test
    public void testGetEmployeeById_Success() {
        // Mock data
        Employee mockEmployee = new Employee();
        mockEmployee.setId("1");
        mockEmployee.setEmployee_name("John");

        when(employeeDao.getEmployeeById("1")).thenReturn(new ResponseEntity<>(mockEmployee, HttpStatus.OK));
        ResponseEntity<Employee> responseEntity = employeeService.getEmployeeById("1");
        // Verify that the method returns a ResponseEntity with HttpStatus.OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Verify that the returned employee matches the mock employee
        assertEquals(mockEmployee, responseEntity.getBody());
    }

    @Test
    public void testGetEmployeeById_Exception() {
        // Stub the employeeDao.getEmployeeById(id) method to throw an exception
        when(employeeDao.getEmployeeById("2")).thenThrow(new RuntimeException("Error occurred"));
        // Call the method being tested
        ResponseEntity<Employee> responseEntity = employeeService.getEmployeeById("2");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

    }

    @Test
    public void testGetHighestSalaryOfEmployees_Success() {
        // Mock data
        List<Employee> employees = getEmployeeList();


        // Stub the employeeDao.getEmployeeDetails() method to return the mock data
        when(employeeDao.getEmployeeDetails()).thenReturn(employees);


        ResponseEntity<Integer> responseEntity = employeeService.getHighestSalaryOfEmployees();

        // Verify that the method returns a ResponseEntity with HttpStatus.OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify that the highest salary is returned correctly
        assertEquals(34344, responseEntity.getBody());
    }

    @Test
    public void testGetHighestSalaryOfEmployees_NoEmployees() {
        // Stub the employeeDao.getEmployeeDetails() method to return null
        when(employeeDao.getEmployeeDetails()).thenReturn(null);

        // Call the method being tested
        ResponseEntity<Integer> responseEntity = employeeService.getHighestSalaryOfEmployees();

        // Verify that the method returns a ResponseEntity with HttpStatus.INTERNAL_SERVER_ERROR
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames_Success() {
        // Mock data
        List<Employee> employees = getEmployeeList();


        when(employeeDao.getEmployeeDetails()).thenReturn(employees);


        ResponseEntity<List<String>> responseEntity = employeeService.getTopTenHighestEarningEmployeeNames();


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());


        List<String> expectedEmployeeNames = List.of("John", "Alice");
        assertEquals(expectedEmployeeNames, responseEntity.getBody());
    }


    @Test
    public void testCreateEmployeeRecord_Success() {
        // Mock data
        Employee newEmployee = new Employee();
        newEmployee.setEmployee_salary(50000);
        newEmployee.setEmployee_name("John");
        newEmployee.setId("1");

        EmployeeResponse<Employee> es = new EmployeeResponse<Employee>();
        es.setStatus(HttpStatus.CREATED.toString());
        es.setData(newEmployee);
        es.setMessage("Employee created successfully");
        // Mock the behavior of employeeDao.createEmployees() method
        Mono<EmployeeResponse<Employee>> employeeResponseMono = Mono.just((es));
        when(employeeDao.createEmployees(newEmployee)).thenReturn(employeeResponseMono);

        // Call the method being tested
        ResponseEntity<Employee> responseEntity = employeeService.createEmployeeRecord(newEmployee);

        // Verify that the method returns a ResponseEntity with HttpStatus.CREATED
        assertEquals(HttpStatus.CREATED.toString(), responseEntity.getStatusCode().toString());

        // Verify that the returned employee matches the input employee
        assertEquals(newEmployee, responseEntity.getBody());
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
