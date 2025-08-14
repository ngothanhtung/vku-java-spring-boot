package com.example.demo.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.CreateStudentRequestDto;
import com.example.demo.dtos.PaginatedStudentResponseDto;
import com.example.demo.dtos.StudentResponseDto;
import com.example.demo.dtos.UpdateStudentRequestDto;
import com.example.demo.enums.StudentStatus;
import com.example.demo.repositories.StudentProjection;
import com.example.demo.services.StudentService;

import jakarta.validation.Valid;

@RestController()
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // @PreAuthorize("hasAnyRole('Administrators', 'Managers')")
    // @PreAuthorize("hasAnyRole('Administrators', 'Managers')")
    @GetMapping()
    public List<StudentResponseDto> getAllStudents() {
        return this.studentService.getAllStudents();
    }

    @GetMapping("/paging")
    public PaginatedStudentResponseDto getAllStudentsPaginated(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        System.out.println("page: " + page);
        System.out.println("size: " + size);
        return this.studentService.getAllStudentsPaginated(page, size);
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

    @DeleteMapping("/soft-delete/{id}")
    public void softDeleteStudent(@PathVariable("id") Long id) {
        this.studentService.softDeleteStudent(id);
    }

    @GetMapping("/get-all/deleted/false")
    public List<StudentResponseDto> findAvailableStudents() {
        return this.studentService.findAvailableStudents();
    }

    @GetMapping("/get-all/status")
    public List<StudentResponseDto> findByStatus(@RequestParam("status") StudentStatus status) {

        return this.studentService.findByStatus(status);
    }

    @GetMapping("/get-all/department/{id}")
    public List<StudentResponseDto> findByDepartment(@PathVariable("id") Long departmentId) {

        return this.studentService.findByDepartmentId(departmentId);
    }

    @GetMapping("/get-all/name")
    public List<StudentProjection> findByName(@RequestParam("name") String name) {
        return this.studentService.findByNameContainingIgnoreCase(name);
    }

    @GetMapping("/get-all/email")
    public List<StudentProjection> findByEmail(@RequestParam("email") String email) {
        return this.studentService.searchByEmailContainingIgnoreCase(email);
    }
}
