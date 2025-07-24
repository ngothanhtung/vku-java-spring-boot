package vku.apiservice.tutorials.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssigneeDto {
    private String id;
    private String name;
    private String email;

    public AssigneeDto(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
