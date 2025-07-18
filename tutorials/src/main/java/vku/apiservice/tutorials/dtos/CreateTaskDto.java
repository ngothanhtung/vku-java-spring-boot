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

    @NotBlank(message = "Id of Assignee is required")
    private String assigneeId;

    public CreateTaskDto(String title, String description, String assigneeId) {
        this.title = title;
        this.description = description;
        this.assigneeId = assigneeId;
    }
}