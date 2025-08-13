package com.example.demo.events;

import com.example.demo.entities.Student;
import lombok.Value;

@Value
public class StudentCreatedEvent {
    private final long studentId;
    private final Student createdStudent;
}