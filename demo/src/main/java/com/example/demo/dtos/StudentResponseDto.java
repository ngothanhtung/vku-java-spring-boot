package com.example.demo.dtos;

import java.util.List;

import com.example.demo.enums.StudentStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentResponseDto {
    private Long id;
    private String name;
    private String email;
    private String address;
    private StudentStatus status;
    private DepartmentResponseDto department;
    private List<CourseResponseDto> courses;
}
