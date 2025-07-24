package vku.apiservice.tutorials.domain.workspace.entities;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vku.apiservice.tutorials.domain.common.entities.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "projects")
public class Project extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String name;
  private String description;
  private Date startDate;
  private Date endDate;

  // One Project can have many Tasks
  @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
  @JsonIgnore
  private List<Task> tasks;
}
