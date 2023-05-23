package com.example.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
public class DepartmentJpaTest {
    @Autowired
    DepartmentRepo departmentRepo;
    @Autowired
    private TestEntityManager testEntityManager;
    private Department finance;

    @BeforeEach
    void setUp() {
        finance = Department.builder().name("Finance").build();
    }

    @Test
    void createDepartment() {
        testEntityManager.persistAndFlush(finance);
        assertEquals(finance.getName(), departmentRepo.findDepartmentByName(finance.getName()).getName());
    }

    @Test
    void deleteDepartment() {
        testEntityManager.persistAndFlush(finance);
        departmentRepo.delete(departmentRepo.findDepartmentByName(finance.getName()));
        assertNull(departmentRepo.findDepartmentByName(finance.getName()));
    }
}
