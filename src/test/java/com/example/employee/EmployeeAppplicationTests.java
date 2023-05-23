package com.example.employee;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeApplicationTests {

    @Autowired
    private TestRestTemplate template;
    private Department finance;
    private Employee saro;
    private Employee krithika;

    @BeforeEach
    void setUp() {
        finance = Department.builder().name("Finance").build();
        saro = Employee.builder()
                .firstName("Sarawanan")
                .lastName("Mahalingam")
                .email("sarawananm@icloud.com")
                .salary(15000F)
                .department(finance).build();
        krithika = Employee.builder()
                .firstName("Krithika")
                .lastName("Sarawanan")
                .email("krithikasarawanan@yahoo.com")
                .salary(10000F)
                .department(finance).build();
    }

    @Test
    void createEmployee() {
        var entity = template.postForEntity("/api/employees", saro, Employee.class);
        assertEquals(HttpStatus.CREATED, entity.getStatusCode());
        assertEquals(saro.getEmail(), Objects.requireNonNull(entity.getBody()).getEmail());
        assertEquals(finance.getName(), entity.getBody().getDepartment().getName());
    }

    @Test
    void getEmployees() throws JsonProcessingException {
        template.postForEntity("/api/employees", saro, Employee.class);
        var entity = template.getForEntity("/api/employees", String.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        var array = new ObjectMapper().readValue(entity.getBody(), Employee[].class);
        assertTrue(Arrays.stream(array).anyMatch(employee -> employee.getFirstName().equals(saro.getFirstName())));
    }

    @Test
    void getEmployeeById() {
        var notFound = template.getForEntity("/api/employees/5", Employee.class);
        assertEquals(HttpStatus.NOT_FOUND, notFound.getStatusCode());
        template.postForEntity("/api/employees", krithika, Employee.class);
        var found = template.getForEntity("/api/employees/3", Employee.class);
        assertEquals(krithika.getFirstName(), Objects.requireNonNull(found.getBody()).getFirstName());
    }

    @Test
    void updateEmployee() {
        template.postForEntity("/api/employees", saro, Employee.class);
        template.put("/api/employees/2", krithika);
        var found = template.getForEntity("/api/employees/2", Employee.class);
        assertEquals(krithika.getFirstName(), Objects.requireNonNull(found.getBody()).getFirstName());
    }

    @Test
    void deleteEmployee() {
        template.postForEntity("/api/employees", saro, Employee.class);
        template.delete("/api/employees/1");
        var notFound = template.getForEntity("/api/employees/1", Employee.class);
        assertEquals(HttpStatus.NOT_FOUND, notFound.getStatusCode());
    }
}
