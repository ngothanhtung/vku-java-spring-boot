package vku.apiservice.tutorials.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectResponseDto {
  private String id;
  private String name;
  private String description;

  public ProjectResponseDto(String id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }
}
