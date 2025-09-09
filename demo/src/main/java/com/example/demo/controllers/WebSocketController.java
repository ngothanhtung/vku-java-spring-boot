package com.example.demo.controllers;

import com.example.demo.dtos.ChatMessage;
import com.example.demo.dtos.NotificationMessage;
import com.example.demo.dtos.TopicJoinMessage;
import com.example.demo.dtos.TopicMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Chat message handling When a message is sent to /app/chat.sendMessage, it will be broadcasted to all users subscribed to /topic/public
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        // Save database ...
        // Code ...
        return chatMessage;
    }

    /**
     * Chat message handling When a message is sent to /app/chat.sendMessage, it will be broadcasted to all users subscribed to /topic/public
     */
    @MessageMapping("/chat.received")
    @SendTo("/topic/public")
    public ChatMessage received(@Payload ChatMessage chatMessage) {
        // Save database ...
        // Code ...

        // Update the message status to "received"
        return chatMessage;
    }

    /**
     * Chat message handling When a message is sent to /app/chat.sendMessage, it will be broadcasted to all users subscribed to /topic/public
     */
    @MessageMapping("/chat.sendMessage.classA1")
    @SendTo("/topic/class/a1")
    public ChatMessage sendMessageToClassA1(@Payload ChatMessage chatMessage) {
        // Save database ...
        // Code ...
        return chatMessage;
    }

    /**
     * Join a dynamic topic
     */
    @MessageMapping("/topic.join")
    public void joinTopic(@Payload TopicJoinMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String topicName = message.getTopicName();
        String username = message.getUsername();

        // Store topic subscription in session
        Set<String> userTopics = (Set<String>) headerAccessor.getSessionAttributes()
                .computeIfAbsent("subscribedTopics", k -> new HashSet<>());
        userTopics.add(topicName);

        // Notify others in the topic about new user
        ChatMessage joinNotification = new ChatMessage();
        joinNotification.setType(ChatMessage.MessageType.JOIN);
        joinNotification.setSender(username);
        joinNotification.setContent(username + " joined the topic");
        joinNotification.setTimestamp(LocalDateTime.now());

        messagingTemplate.convertAndSend("/topic/dynamic/" + topicName, joinNotification);
    }


    /**
     * Leave a dynamic topic
     */
    @MessageMapping("/topic.leave")
    public void leaveTopic(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String topicName = message.getTopicName();
        String username = message.getUsername();

        // Remove topic from session
        Set<String> userTopics = (Set<String>) headerAccessor.getSessionAttributes().get("subscribedTopics");
        if (userTopics != null) {
            userTopics.remove(topicName);
        }

        // Notify others in the topic about user leaving
        ChatMessage leaveNotification = new ChatMessage();
        leaveNotification.setType(ChatMessage.MessageType.LEAVE);
        leaveNotification.setSender(username);
        leaveNotification.setContent(username + " left the topic");
        leaveNotification.setTimestamp(LocalDateTime.now());

        messagingTemplate.convertAndSend("/topic/dynamic/" + topicName, leaveNotification);
    }

    /**
     * Send message to dynamic topic
     */
    @MessageMapping("/topic.sendMessage")
    public void sendMessageToTopic(@Payload TopicMessage message) {
        String topicName = message.getTopicName();

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.CHAT);
        chatMessage.setSender(message.getSender());
        chatMessage.setContent(message.getContent());
        chatMessage.setTimestamp(LocalDateTime.now());

        // Send to dynamic topic
        messagingTemplate.convertAndSend("/topic/dynamic/" + topicName, chatMessage);
    }


    /**
     * Create a new dynamic topic
     */
    @MessageMapping("/topic.create")
    public void createTopic(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String topicName = message.getTopicName();
        String creator = message.getUsername();

        // Add creator to topic
        Set<String> userTopics = (Set<String>) headerAccessor.getSessionAttributes()
                .computeIfAbsent("subscribedTopics", k -> new HashSet<String>());
        userTopics.add(topicName);

        // Send creation confirmation
        ChatMessage createNotification = new ChatMessage();
        createNotification.setType(ChatMessage.MessageType.INFO);
        createNotification.setSender("System");
        createNotification.setContent("Topic '" + topicName + "' created by " + creator);
        createNotification.setTimestamp(LocalDateTime.now());

        messagingTemplate.convertAndSend("/topic/dynamic/" + topicName, createNotification);
    }


    /**
     * User join handling When a user joins, it will be broadcasted to all users
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
}
