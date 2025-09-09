package com.example.demo.dtos;

// TopicJoinMessage.java
public class TopicJoinMessage {
    private String topicName;
    private String username;

    // constructors, getters, setters
    public TopicJoinMessage() {
    }

    public TopicJoinMessage(String topicName, String username) {
        this.topicName = topicName;
        this.username = username;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}