package com.example.employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
@DataJpaTest
@ExtendWith(SpringExtension.class)
public class EmployeeJpaTest {
    @Autowired
    TestEntityManager testEntityManager;
    @Autowired
    EmployeeRepo employeeRepo;
    @Autowired
    DepartmentRepo departmentRepo;
    private Department finance;
    private Employee saro;
    @BeforeEach
    void setUp() {
        finance = Department.builder().name("Finance").build();
        saro = Employee.builder()
                .firstName("Sarawanan")
                .lastName("Mahalingam")
                .email("sarawananm@icloud.com")
                .salary(15000F).build();
    }

    @Test
    void createEmployee() {
        testEntityManager.persistAndFlush(finance);
        saro.setDepartment(departmentRepo.findDepartmentByName(finance.getName()));
        testEntityManager.persistAndFlush(saro);
        var foundEmployee = employeeRepo.findEmployeeByFirstName(saro.getFirstName());
        assertEquals(saro.getEmail(), foundEmployee.getEmail());
        assertEquals(finance.getName(), foundEmployee.getDepartment().getName());
    }

    @Test
    void deleteEmployee() {
        testEntityManager.persistAndFlush(saro);
        employeeRepo.delete(employeeRepo.findEmployeeByFirstName(saro.getFirstName()));
        assertNull(employeeRepo.findEmployeeByFirstName(saro.getFirstName()));
    }

}
