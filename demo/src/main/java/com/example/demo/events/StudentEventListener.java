package com.example.demo.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StudentEventListener {
    @EventListener
    public void handleStudentUpdatedEvent(StudentUpdatedEvent event) {

        System.out.println("🔥 StudentEventListener handleStudentUpdatedEvent");
        System.out.printf("🔥 Student with ID %d has been updated: %s%n",
                event.getStudentId(),
                event.getUpdatedStudent().getName());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleStudentDeletedEvent(StudentDeletedEvent event) {

        System.out.println("🔥 StudentEventListener handleStudentDeletedEvent");
        System.out.printf("🔥 Student with ID %d has been deleted", event.getStudentId());
        // Here you can add additional logic, such as sending notifications,
    }

    @EventListener
    public void handleStudentCreatedEvent(StudentCreatedEvent event) {
        System.out.println("🔥 StudentEventListener handleStudentCreatedEvent");
        System.out.printf("🔥 Student with ID %d has been created: %s%n",
                event.getStudentId(),
                event.getCreatedStudent().getName());
        // Here you can add additional logic, such as sending notifications,
    }
}