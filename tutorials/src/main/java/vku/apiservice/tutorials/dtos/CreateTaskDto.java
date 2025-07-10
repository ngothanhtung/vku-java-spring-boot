package vku.apiservice.tutorials.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateTaskDto {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    public CreateTaskDto(String title, String description) {
        this.title = title;
        this.description = description;

    }
}