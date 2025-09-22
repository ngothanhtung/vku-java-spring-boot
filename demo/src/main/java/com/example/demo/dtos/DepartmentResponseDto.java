package com.example.demo.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO for department response")
@Data
@NoArgsConstructor
public class DepartmentResponseDto {

    @Schema(description = "Unique identifier of the department", example = "1")
    private Long id;

    @Schema(description = "Name of the department", example = "Computer Science")
    private String name;
}
