package com.example.demo.services;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Employee;
import com.example.demo.repositories.EmployeeJpaRepository;

@Service
public class EmployeeService {
    private final EmployeeJpaRepository employeeJpaRepository;

    public EmployeeService(EmployeeJpaRepository employeeJpaRepository) {
        this.employeeJpaRepository = employeeJpaRepository;
    }

    public Employee createEmployee(Employee employee) {
        return this.employeeJpaRepository.save(employee);
    }

    public Employee getEmployeeById(Long id) {
        return this.employeeJpaRepository.findById(id).orElseThrow();
    }

}
