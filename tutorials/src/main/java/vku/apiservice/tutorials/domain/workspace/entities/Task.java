package vku.apiservice.tutorials.domain.workspace.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vku.apiservice.tutorials.domain.common.entities.BaseEntity;
import vku.apiservice.tutorials.domain.security.entities.User;
import vku.apiservice.tutorials.domain.workspace.enums.TaskPriority;
import vku.apiservice.tutorials.domain.workspace.enums.TaskStatus;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private String description;

    private Date startDate;
    private Date dueDate;

    private Date completedDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TaskPriority priority;

    // Many Tasks can be assigned to one User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id") // Optional: specify the foreign key column
    private User assignee;

    // Many Tasks can belong to one Project (nullable)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id") // Optional: specify the foreign key column
    private Project project;

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = TaskStatus.TO_DO;
        }
        if (this.priority == null) {
            this.priority = TaskPriority.MEDIUM;
        }
    }
}
