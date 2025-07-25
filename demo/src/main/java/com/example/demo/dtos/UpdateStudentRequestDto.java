package com.example.demo.dtos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentRequestDto {
    private String name;

    @Email(message = "Email is invalid")
    private String email;

    private String address;

    private String password;
}
