package com.example.demo.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class DepartmentResponseDto {
    private Long id;
    private String name;
}
