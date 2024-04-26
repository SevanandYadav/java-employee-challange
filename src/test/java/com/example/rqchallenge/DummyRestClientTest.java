package com.example.rqchallenge;

import com.example.rqchallenge.utility.client.ApiExampleRestClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DummyRestClientTest {

    @Mock
    private WebClient webClient;

    @InjectMocks
    private ApiExampleRestClient restClient;

    @Test
    public void testGetEmployees_success() throws Exception {

//        // Mock response data (replace with actual data structure if needed)
//        Map<String, Object> responseData = new HashMap<>();
//        responseData.put("status", "success");
//        List<Object> employeeList = new ArrayList<>();
//        // ... add employee data to employeeList ...
//        responseData.put("data", employeeList);
//
//        // Mock successful response
//        Mono<ResponseEntity<Object>> responseMono = Mono.just(ResponseEntity.ok(responseData));
//        Mockito.when(webClient.get()
//                .uri("https://dummy.restapiexample.com/api/v1/employees")
//                .retrieve()
//                .bodyToMono(ResponseEntity.class))
//                .thenReturn(responseMono);
//
//        // Call the method under test
//        Mono<ResponseEntity> actualResponse = restClient.getEmployees();
//
//        // Assertions
//        actualResponse.subscribe(response -> {
//            assertEquals(HttpStatus.OK, response.getStatusCode());
//            // Assert response body structure and content if necessary (based on responseData)
//        });
    }
}
