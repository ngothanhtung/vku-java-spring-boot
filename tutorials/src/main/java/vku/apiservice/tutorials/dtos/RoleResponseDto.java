package vku.apiservice.tutorials.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing user information in API responses.
 * Contains user basic information and associated roles.
 */
@Data
@Builder
@NoArgsConstructor
public class RoleResponseDto {
    private String id;
    private String code;
    private String name;

    public RoleResponseDto(String id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}