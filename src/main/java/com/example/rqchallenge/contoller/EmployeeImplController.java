package com.example.rqchallenge.contoller;

import com.example.rqchallenge.entity.Employee;
import com.example.rqchallenge.entity.response.EmployeeDeleteResponse;
import com.example.rqchallenge.service.EmployeeService;
import com.example.rqchallenge.utility.EmployeeUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class EmployeeImplController implements IEmployeeController {


    private final EmployeeService employeeService;

    @Autowired
    public EmployeeImplController(EmployeeService employeeService) {

        this.employeeService = employeeService;
    }

    @Override
    @GetMapping()
    public ResponseEntity<List<Employee>> getAllEmployees() {

        return employeeService.getEmployeeDetails();

    }

    @Override
    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) {
        return employeeService.getEmployeeByName(searchString);

    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        return employeeService.getEmployeeById(id);

    }

    @Override
    @GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return employeeService.getHighestSalaryOfEmployees();


    }

    @Override
    @GetMapping("/topTenHighestEarningEmployeeNames")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return employeeService.getTopTenHighestEarningEmployeeNames();


    }

    @Override
    @PostMapping()
    public ResponseEntity<Employee> createEmployee(@RequestBody Map<String, Object> employeeInput) {

        Employee newEmployee = EmployeeUtility.createEmployee(employeeInput);
        // Validate input data
        if (newEmployee.getEmployee_name() == null || newEmployee.getEmployee_salary() < 0 || newEmployee.getEmployee_age() < 0) {
            return ResponseEntity.badRequest().build();
        }
        return employeeService.createEmployeeRecord(newEmployee);


    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeDeleteResponse> deleteEmployeeById(@PathVariable String id) {

        EmployeeDeleteResponse response = employeeService.deleteEmployeeById(id).block();

        return new ResponseEntity<>(response, HttpStatus.OK);


    }
}
