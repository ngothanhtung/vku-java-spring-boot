package com.example.demo.events;

import com.example.demo.entities.Student;
import lombok.Value;

@Value
public class StudentUpdatedEvent {
    private final long studentId;
    private final Student updatedStudent;
}