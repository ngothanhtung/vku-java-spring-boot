package com.example.demo.dtos;

import java.util.List;

import com.example.demo.enums.StudentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO for student response")
@Data
@NoArgsConstructor
public class StudentResponseDto {

    @Schema(description = "Unique identifier of the student", example = "1")
    private Long id;

    @Schema(description = "Full name of the student", example = "John Doe")
    private String name;

    @Schema(description = "Email address of the student", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Physical address of the student", example = "123 Main Street, Springfield, IL")
    private String address;

    @Schema(description = "Current status of the student", example = "ACTIVE")
    private StudentStatus status;

    @Schema(description = "Department information where the student is enrolled")
    private DepartmentResponseDto department;

    @Schema(description = "List of courses the student is enrolled in")
    private List<CourseResponseDto> courses;
}
