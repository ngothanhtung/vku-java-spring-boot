package vku.apiservice.tutorials.domain.security.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * DTO for representing user information in API responses.
 * Contains user basic information and associated roles.
 */
@Data
@Builder
@NoArgsConstructor
public class RoleResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String code;
    private String name;

    public RoleResponseDto(String id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}