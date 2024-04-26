package com.example.rqchallenge.utility.client;

import com.example.rqchallenge.constants.APIConstants;
import com.example.rqchallenge.entity.Employee;
import com.example.rqchallenge.entity.response.EmployeeDeleteResponse;
import com.example.rqchallenge.entity.response.EmployeeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class ApiExampleRestClient {

    private final WebClient webClient;

    @Autowired
    public ApiExampleRestClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(APIConstants.BASE_URL.getValue()).build();
    }


    public Mono<EmployeeResponse<List<Employee>>> getEmployees() {
        return webClient.get()
                .uri(APIConstants.EMPLOYEE.getValue())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<EmployeeResponse<List<Employee>>>() {
                });

    }


    public Mono<EmployeeResponse<Employee>> getEmployeeById(String empId) {
        return webClient.get()
                .uri(APIConstants.EMPLOYEE_BY_ID.getValue() + empId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<EmployeeResponse<Employee>>() {
                });

    }

    public Mono<EmployeeResponse<Employee>> createEmployees(Employee newEmployee) {
        log.debug("going to create employees {} ", newEmployee);
        return webClient.post()
                .uri(APIConstants.CREATE_EMPLOYEE.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newEmployee), Employee.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<EmployeeResponse<Employee>>() {
                });

    }

    public Mono<EmployeeDeleteResponse> deleteEmployeeById(String deleteId) {
        return webClient.delete()
                .uri(APIConstants.DELETE_EMPLOYEE.getValue() + deleteId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<EmployeeDeleteResponse>() {
                });
    }

}