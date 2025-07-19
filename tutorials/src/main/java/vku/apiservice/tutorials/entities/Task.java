package vku.apiservice.tutorials.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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

    @Column(length = 20)
    private String status;

    @Column(length = 20)
    private String priority;

    // Many Tasks can be assigned to one User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id") // Optional: specify the foreign key column
    private User assignee;

    @PrePersist
    public void prePersist() {
        if (this.status == null || this.status.trim().isEmpty()) {
            this.status = "TO_DO";
        }
        if (this.priority == null || this.priority.trim().isEmpty()) {
            this.priority = "MEDIUM";
        }
    }
}
