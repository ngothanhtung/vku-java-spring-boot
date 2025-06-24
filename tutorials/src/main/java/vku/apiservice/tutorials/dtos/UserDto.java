package vku.apiservice.tutorials.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserDto {
    private String id;
    private String name;
    private String email;

    public UserDto(String id, String name, String email, List<RoleDto> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    private List<RoleDto> roles;
}
