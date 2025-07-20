package vku.apiservice.tutorials.exceptions;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class ErrorResponseTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testErrorResponseWithStringArray() throws Exception {
    List<String> messages = Arrays.asList(
        "Title is required",
        "Invalid task priority. Valid values are: LOW, MEDIUM, HIGH, URGENT",
        "Invalid task status. Valid values are: TO_DO, IN_PROGRESS, DONE");

    ErrorResponse errorResponse = new ErrorResponse(400, messages, "Bad Request");

    // Convert to JSON to see the format
    String json = objectMapper.writeValueAsString(errorResponse);
    System.out.println("Error Response JSON:");
    System.out.println(json);

    // Verify the structure
    assertEquals(400, errorResponse.getStatus());
    assertEquals("Bad Request", errorResponse.getError());
    assertTrue(errorResponse.getMessage() instanceof List<?>);

    @SuppressWarnings("unchecked")
    List<String> messageList = (List<String>) errorResponse.getMessage();
    assertEquals(3, messageList.size());
    assertEquals("Title is required", messageList.get(0));
    assertEquals("Invalid task priority. Valid values are: LOW, MEDIUM, HIGH, URGENT", messageList.get(1));
    assertEquals("Invalid task status. Valid values are: TO_DO, IN_PROGRESS, DONE", messageList.get(2));
  }

  @Test
  void testErrorResponseWithSingleString() throws Exception {
    ErrorResponse errorResponse = new ErrorResponse(404, "User not found", "Not Found");

    // Convert to JSON to see the format
    String json = objectMapper.writeValueAsString(errorResponse);
    System.out.println("Single Error Response JSON:");
    System.out.println(json);

    // Verify the structure
    assertEquals(404, errorResponse.getStatus());
    assertEquals("Not Found", errorResponse.getError());
    assertTrue(errorResponse.getMessage() instanceof String);
    assertEquals("User not found", errorResponse.getMessage());
  }
}
