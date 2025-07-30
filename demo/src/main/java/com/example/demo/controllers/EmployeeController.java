package com.example.demo.controllers;

import com.example.demo.dtos.CreateStudentRequestDto;
import com.example.demo.dtos.PaginatedStudentResponseDto;
import com.example.demo.dtos.StudentResponseDto;
import com.example.demo.dtos.UpdateStudentRequestDto;
import com.example.demo.entities.AuthUser;
import com.example.demo.entities.Employee;
import com.example.demo.services.EmployeeService;
import com.example.demo.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable("id") Long id) {
        return this.employeeService.getEmployeeById(id);
    }

}
