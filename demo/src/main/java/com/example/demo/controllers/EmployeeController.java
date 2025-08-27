package com.example.demo.controllers;

import com.example.demo.entities.Employee;
import com.example.demo.services.EmployeeService;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping()
    public Employee createEmployee(@RequestBody Employee data) {
        return this.employeeService.createEmployee(data);
    }

    @PatchMapping("/{id}")
    public Employee updateEmployee(@PathVariable("id") Long id, @RequestBody Employee data) {
        return this.employeeService.updateEmployee(id, data);
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable("id") Long id) {
        return this.employeeService.getEmployeeById(id);
    }
}
