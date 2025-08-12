package com.example.demo.events;

import lombok.Value;

@Value
public class StudentDeletedEvent {
    private final long studentId;
}