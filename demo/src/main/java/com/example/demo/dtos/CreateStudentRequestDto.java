package com.example.demo.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Schema(description = "DTO for creating a new student")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentRequestDto {

    @Schema(description = "Name of the student", example = "John Doe")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "Email of the student", example = "johndoe@gmail.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    @Schema(description = "Address of the student", example = "123 Main St, City, Country")
    private String address;

    @Schema(description = "Password for the student account", example = "P@ssw0rd!")
    @NotBlank(message = "Password is required")
    private String password;

    @Schema(description = "Status of the student", example = "ACTIVE")
    private String status;

    @Schema(description = "ID of the department the student belongs to", example = "1")
    private Long departmentId;
}
