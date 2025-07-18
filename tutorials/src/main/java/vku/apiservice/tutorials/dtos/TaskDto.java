package vku.apiservice.tutorials.dtos;

import lombok.Getter;
import lombok.Setter;
import vku.apiservice.tutorials.entities.User;

import java.util.Date;

@Getter
@Setter
public class TaskDto {
    private String id;
    private String title;
    private String description;

    private Date startDate;
    private Date dueDate;

    private Date completedDate;

    private String status;
    private String priority;

    private User assignee;
}