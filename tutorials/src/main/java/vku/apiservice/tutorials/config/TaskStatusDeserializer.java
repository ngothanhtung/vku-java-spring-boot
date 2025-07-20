package vku.apiservice.tutorials.config;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

import vku.apiservice.tutorials.enums.TaskStatus;

/**
 * Custom deserializer for TaskStatus enum with improved error messages
 */
public class TaskStatusDeserializer extends JsonDeserializer<TaskStatus> {

  @Override
  public TaskStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    String value = p.getText();

    if (value == null) {
      return null; // Allow null values
    }

    try {
      return TaskStatus.fromString(value);
    } catch (IllegalArgumentException e) {
      throw JsonMappingException.from(p, "Invalid task status: '" + value + "'. Valid values are: TO_DO, IN_PROGRESS, DONE");
    }
  }
}
