package vku.apiservice.tutorials.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectSummaryDto {
  private String id;
  private String name;
  private String description;

  public ProjectSummaryDto(String id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }
}
