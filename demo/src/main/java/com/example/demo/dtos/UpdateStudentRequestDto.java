package com.example.demo.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO for updating an existing student")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentRequestDto {

    @Schema(description = "Updated name of the student", example = "Jane Smith")
    private String name;

    @Schema(description = "Updated email address of the student", example = "jane.smith@example.com")
    @Email(message = "Email is invalid")
    private String email;

    @Schema(description = "Updated address of the student", example = "456 Oak Avenue, Springfield, IL")
    private String address;

    @Schema(description = "Updated password for the student account", example = "NewP@ssw0rd!")
    private String password;

    @Schema(description = "Updated status of the student", example = "INACTIVE")
    private String status;

    @Schema(description = "Updated department ID the student belongs to", example = "2")
    private Long departmentId;
}
