package vku.apiservice.tutorials.domain.workspace.dtos;

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
