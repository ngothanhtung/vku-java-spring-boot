package vku.apiservice.tutorials.config;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

import vku.apiservice.tutorials.enums.TaskPriority;

/**
 * Custom deserializer for TaskPriority enum with improved error messages
 */
public class TaskPriorityDeserializer extends JsonDeserializer<TaskPriority> {

  @Override
  public TaskPriority deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    String value = p.getText();

    if (value == null) {
      return null; // Allow null values
    }

    try {
      return TaskPriority.fromString(value);
    } catch (IllegalArgumentException e) {
      throw JsonMappingException.from(p, "Invalid task priority: '" + value + "'. Valid values are: LOW, MEDIUM, HIGH, URGENT");
    }
  }
}
