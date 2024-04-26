package com.example.rqchallenge;

import com.example.rqchallenge.contoller.IEmployeeController;
import com.example.rqchallenge.entity.Employee;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class RqChallengeApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEmployeeController employeeController;


    @Test
    public void testGetAllEmployees() throws Exception {
        ResponseEntity<List<Employee>> responseEntity = getListResponseEntity();
        Mockito.when(employeeController.getAllEmployees()).thenReturn(responseEntity);

        // Performing the request and validating the response
        mockMvc.perform(MockMvcRequestBuilders.get("/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employee_name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].employee_name").value("Alice"));
    }

    private static ResponseEntity<List<Employee>> getListResponseEntity() {
        Employee e1 = new Employee();
        e1.setEmployee_name("John");
        Employee e2 = new Employee();
        e2.setEmployee_name("Alice");

        List<Employee> employees = Arrays.asList(e1, e2);

        return new ResponseEntity<>(employees, HttpStatus.OK);

    }

    @Test
    public void testGetAllEmployeesInvoke() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/").contentType(MediaType.APPLICATION_JSON));

        // Verify that getAllEmployees was called
        Mockito.verify(employeeController).getAllEmployees();

    }
}
