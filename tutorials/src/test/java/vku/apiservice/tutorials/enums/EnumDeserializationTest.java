package vku.apiservice.tutorials.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import vku.apiservice.tutorials.dtos.CreateTaskDto;

class EnumDeserializationTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testValidTaskStatusDeserialization() throws Exception {
    String json = """
        {
            "title": "Test Task",
            "description": "Test Description",
            "status": "TO_DO",
            "priority": "HIGH",
            "assigneeId": "user-123"
        }
        """;

    CreateTaskDto dto = objectMapper.readValue(json, CreateTaskDto.class);
    assertEquals(TaskStatus.TO_DO, dto.getStatus());
    assertEquals(TaskPriority.HIGH, dto.getPriority());
  }

  @Test
  void testCaseInsensitiveDeserialization() throws Exception {
    String json = """
        {
            "title": "Test Task",
            "description": "Test Description",
            "status": "in_progress",
            "priority": "low",
            "assigneeId": "user-123"
        }
        """;

    CreateTaskDto dto = objectMapper.readValue(json, CreateTaskDto.class);
    assertEquals(TaskStatus.IN_PROGRESS, dto.getStatus());
    assertEquals(TaskPriority.LOW, dto.getPriority());
  }

  @Test
  void testInvalidTaskStatusDeserialization() {
    String json = """
        {
            "title": "Test Task",
            "description": "Test Description",
            "status": "INVALID_STATUS",
            "assigneeId": "user-123"
        }
        """;

    Exception exception = assertThrows(Exception.class, () -> {
      objectMapper.readValue(json, CreateTaskDto.class);
    });

    assertTrue(exception.getMessage().contains("Invalid task status"));
    assertTrue(exception.getMessage().contains("INVALID_STATUS"));
    assertTrue(exception.getMessage().contains("TO_DO, IN_PROGRESS, DONE"));
  }

  @Test
  void testInvalidTaskPriorityDeserialization() {
    String json = """
        {
            "title": "Test Task",
            "description": "Test Description",
            "priority": "SUPER_HIGH",
            "assigneeId": "user-123"
        }
        """;

    Exception exception = assertThrows(Exception.class, () -> {
      objectMapper.readValue(json, CreateTaskDto.class);
    });

    assertTrue(exception.getMessage().contains("Invalid task priority"));
    assertTrue(exception.getMessage().contains("SUPER_HIGH"));
    assertTrue(exception.getMessage().contains("LOW, MEDIUM, HIGH, URGENT"));
  }

  @Test
  void testNullValues() throws Exception {
    String json = """
        {
            "title": "Test Task",
            "description": "Test Description",
            "status": null,
            "priority": null,
            "assigneeId": "user-123"
        }
        """;

    CreateTaskDto dto = objectMapper.readValue(json, CreateTaskDto.class);
    assertNull(dto.getStatus());
    assertNull(dto.getPriority());
  }
}
