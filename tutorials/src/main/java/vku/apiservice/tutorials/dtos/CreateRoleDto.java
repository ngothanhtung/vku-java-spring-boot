package vku.apiservice.tutorials.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CreateRoleDto {
    @NotBlank(message = "Name is required")
    private String name;
}