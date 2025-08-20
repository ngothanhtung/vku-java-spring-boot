package com.example.demo.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dtos.ChatMessage;
import com.example.demo.dtos.NotificationMessage;

@Controller
public class WebSocketController {

  private final SimpMessagingTemplate messagingTemplate;

  public WebSocketController(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  /**
   * Chat message handling
   * When a message is sent to /app/chat.sendMessage, it will be broadcasted to
   * all users subscribed to /topic/public
   */
  @MessageMapping("/chat.sendMessage")
  @SendTo("/topic/public")
  public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
    return chatMessage;
  }

  /**
   * User join handling
   * When a user joins, it will be broadcasted to all users
   */
  @MessageMapping("/chat.addUser")
  @SendTo("/topic/public")
  public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
    // Add username in web socket session
    headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
    return chatMessage;
  }

  /**
   * Send notification to specific user
   */
  public void sendNotificationToUser(String username, String message) {
    NotificationMessage notification = new NotificationMessage(
        message,
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
        "INFO");
    messagingTemplate.convertAndSendToUser(username, "/queue/notifications", notification);
  }

  /**
   * Send notification to all users
   */
  public void sendNotificationToAll(String message) {
    NotificationMessage notification = new NotificationMessage(
        message,
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
        "BROADCAST");
    messagingTemplate.convertAndSend("/topic/notifications", notification);
  }

  /**
   * Demo endpoint to test notifications
   */
  @GetMapping("/api/test-notification")
  public String testNotification() {
    sendNotificationToAll("This is a test notification sent to all users!");
    return "Notification sent successfully!";
  }
}
