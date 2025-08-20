package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
  private String content;
  private String sender;
  private MessageType type;

  public enum MessageType {
    CHAT,
    JOIN,
    LEAVE
  }
}
