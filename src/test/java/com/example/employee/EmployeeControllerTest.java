package com.example.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class EmployeeControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private EmployeeRepo employeeRepo;
    @MockBean
    private DepartmentRepo departmentRepo;
    private Employee saro, krithika;
    private Department finance;

    @BeforeEach
    void setUp() {
        finance = Department.builder().id(1L).name("Finance").build();
        saro = Employee.builder()
                .id(1L)
                .firstName("Sarawanan")
                .lastName("Mahalingam")
                .email("sarawananm@icloud.com")
                .salary(15000F)
                .department(finance).build();
        krithika = Employee.builder()
                .firstName("Krithika")
                .lastName("Sarawanan")
                .email("krithikasarawabab@yahoo.com")
                .salary(10000F)
                .department(finance).build();
    }

    @Test
    void createEmployee() throws Exception {
        Mockito.when(employeeRepo.save(any()))
                .thenReturn(saro);
        mvc.perform(post("/api/employees")
                        .content(new ObjectMapper().writeValueAsString(saro))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(saro.getFirstName()))
                .andExpect(jsonPath("$.department.name").value(finance.getName()));
    }

    @Test
    void getEmployees() throws Exception {
        Mockito.when(employeeRepo.findAll()).thenReturn(List.of(saro));
        mvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value(saro.getFirstName()));
    }

    @Test
    void getEmployeeById() throws Exception {
        Mockito.when(employeeRepo.findById(any())).thenReturn(Optional.ofNullable(saro));
        mvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(saro.getFirstName()));
    }

    @Test
    void getEmployeeByIdNotFound() throws Exception {
        mvc.perform(get("/api/employees/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateEmployee() throws Exception {
        Mockito.when(employeeRepo.findById(any())).thenReturn(Optional.ofNullable(saro));
        Mockito.when(employeeRepo.save(any())).thenReturn(krithika);
        mvc.perform(put("/api/employees/1")
                        .content(new ObjectMapper().writeValueAsString(krithika))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(krithika.getFirstName()));

    }

    @Test
    void deleteEmployee() throws Exception {
        Mockito.when(employeeRepo.findById(any())).thenReturn(Optional.ofNullable(saro));
        mvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());
    }
}
