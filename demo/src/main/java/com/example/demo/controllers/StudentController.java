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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController()
@RequestMapping("/api/students")
@Tag(name = "Student Management", description = "APIs for managing students with role-based access control")
@SecurityRequirement(name = "bearerAuth")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Get all students", description = "Retrieve all students from the database. Requires Administrator or Manager role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Access denied - insufficient privileges", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT token", content = @Content)
    })
    @GetMapping()
    public List<StudentResponseDto> getAllStudents() {
        return this.studentService.getAllStudents();
    }

    @Operation(summary = "Get paginated students", description = "Retrieve students with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paginated students retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedStudentResponseDto.class)))
    })
    @GetMapping("/paging")
    public PaginatedStudentResponseDto getAllStudentsPaginated(
            @Parameter(description = "Page number (1-based)", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of items per page", example = "5") @RequestParam(defaultValue = "5") int size) {
        System.out.println("page: " + page);
        System.out.println("size: " + size);
        return this.studentService.getAllStudentsPaginated(page, size);
    }

    @Operation(summary = "Create a new student", description = "Create a new student record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PostMapping()
    public StudentResponseDto createStudent(@RequestBody @Valid CreateStudentRequestDto createStudentRequestDto) {
        return this.studentService.createStudent(createStudentRequestDto);
    }

    @Operation(summary = "Get student by ID", description = "Retrieve a specific student by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Student not found", content = @Content)
    })
    @GetMapping("/{id}")
    public StudentResponseDto getStudentById(
            @Parameter(description = "Student ID", example = "1") @PathVariable("id") Long id) {
        return this.studentService.getStudentById(id);
    }

    @Operation(summary = "Update student", description = "Update an existing student's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Student not found", content = @Content)
    })
    @PatchMapping("/{id}")
    public StudentResponseDto updateStudent(
            @Parameter(description = "Student ID", example = "1") @PathVariable("id") Long id,
            @RequestBody UpdateStudentRequestDto student) {
        return this.studentService.updateStudent(id, student);
    }

    @Operation(summary = "Delete student", description = "Permanently delete a student record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public void deleteStudent(
            @Parameter(description = "Student ID", example = "1") @PathVariable("id") Long id) {
        this.studentService.deleteStudent(id);
    }

    @Operation(summary = "Soft delete student", description = "Mark a student as deleted without removing from database")
    @DeleteMapping("/soft-delete/{id}")
    public void softDeleteStudent(
            @Parameter(description = "Student ID", example = "1") @PathVariable("id") Long id) {
        this.studentService.softDeleteStudent(id);
    }

    @Operation(summary = "Get available students", description = "Retrieve all students that are not soft-deleted")
    @GetMapping("/get-all/deleted/false")
    public List<StudentResponseDto> findAvailableStudents() {
        return this.studentService.findAvailableStudents();
    }

    @Operation(summary = "Get students by status", description = "Retrieve students filtered by their status")
    @GetMapping("/get-all/status")
    public List<StudentResponseDto> findByStatus(
            @Parameter(description = "Student status", example = "ACTIVE") @RequestParam("status") StudentStatus status) {
        return this.studentService.findByStatus(status);
    }

    @Operation(summary = "Get students by department", description = "Retrieve students from a specific department")
    @GetMapping("/get-all/department/{id}")
    public List<StudentResponseDto> findByDepartment(
            @Parameter(description = "Department ID", example = "1") @PathVariable("id") Long departmentId) {
        return this.studentService.findByDepartmentId(departmentId);
    }

    @Operation(summary = "Search students by name", description = "Search for students by name (case-insensitive, partial match)")
    @GetMapping("/get-all/name")
    public List<StudentProjection> findByName(
            @Parameter(description = "Name to search for", example = "John") @RequestParam("name") String name) {
        return this.studentService.findByNameContainingIgnoreCase(name);
    }

    @Operation(summary = "Search students by email", description = "Search for students by email (case-insensitive, partial match)")
    @GetMapping("/get-all/email")
    public List<StudentProjection> findByEmail(
            @Parameter(description = "Email to search for", example = "john@example.com") @RequestParam("email") String email) {
        return this.studentService.searchByEmailContainingIgnoreCase(email);
    }
}
