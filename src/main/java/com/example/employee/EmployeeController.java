package com.example.employee;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    final EmployeeRepo employeeRepo;
    final DepartmentRepo departmentRepo;

    public EmployeeController(EmployeeRepo employeeRepo, DepartmentRepo departmentRepo) {
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/employees", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Employee createEmployee(@Validated @RequestBody Employee employee) {
        if (!Objects.isNull(employee.getDepartment())) {
            employee.setDepartment(departmentRepo.save(employee.getDepartment()));
        }
        return employeeRepo.save(employee);
    }

    @GetMapping("/employees")
    public Iterable<Employee> getEmployees() {
        return employeeRepo.findAll();
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployeeById(@PathVariable("id") Long id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/employees/{id}")
    public Employee updateEmployeeById(
            @PathVariable("id") Long id,
            @RequestBody Employee employee) {
        var emp = employeeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        emp.setFirstName(employee.getFirstName());
        emp.setLastName(employee.getLastName());
        emp.setEmail(employee.getEmail());
        return employeeRepo.save(emp);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/employees/{id}")
    public void deleteEmployee(@PathVariable("id") Long id) {
        var employee = employeeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        employeeRepo.delete(employee);
    }
}
