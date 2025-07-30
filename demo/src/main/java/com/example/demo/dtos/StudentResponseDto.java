package com.example.demo.dtos;

import com.example.demo.enums.StudentStatus;
import lombok.Builder;
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
}
