package vku.apiservice.tutorials.application.dtos.workspace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssigneeResponseDto {
    private String id;
    private String name;
    private String email;
}
