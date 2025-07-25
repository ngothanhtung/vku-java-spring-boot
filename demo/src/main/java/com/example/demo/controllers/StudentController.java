package com.example.demo.controllers;

import java.util.List;

import com.example.demo.dtos.UpdateStudentRequestDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.CreateStudentRequestDto;
import com.example.demo.dtos.StudentResponseDto;
import com.example.demo.entities.Student;
import com.example.demo.services.StudentService;

import jakarta.validation.Valid;

@RestController()
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping()
    public List<StudentResponseDto> getAllCategories() {
        return this.studentService.getAllCategories();
    }

    @PostMapping()
    public StudentResponseDto createStudent(@RequestBody @Valid CreateStudentRequestDto createStudentRequestDto) {
        return this.studentService.createStudent(createStudentRequestDto);
    }

    @GetMapping("/{id}")
    public StudentResponseDto getStudentById(@PathVariable("id") Long id) {
        return this.studentService.getStudentById(id);
    }

    @PatchMapping("/{id}")
    public StudentResponseDto updateStudent(@PathVariable("id") Long id, @RequestBody UpdateStudentRequestDto student) {
        return this.studentService.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable("id") Long id) {
        this.studentService.deleteStudent(id);
    }
}
