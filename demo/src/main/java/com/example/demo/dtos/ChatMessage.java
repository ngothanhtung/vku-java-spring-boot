package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String topicName;
    private String username;
    private String content;
    private String sender;
    private MessageType type;
    private LocalDateTime Timestamp;

    public enum MessageType {
        INFO,
        CHAT,
        JOIN,
        LEAVE
    }
}
