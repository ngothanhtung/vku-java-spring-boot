# Validation Error Response Format

## Overview

When validation errors occur in API requests, the system now returns a structured error response with multiple error messages in an array format.

## Error Response Structure

```json
{
    "status": 400,
    "message": [
        "Title is required",
        "Invalid task priority. Valid values are: LOW, MEDIUM, HIGH, URGENT",
        "Invalid task status. Valid values are: TO_DO, IN_PROGRESS, DONE"
    ],
    "error": "Bad Request"
}
```

## Example Scenarios

### 1. Complete Validation Failure

**Request:**

```json
POST /api/tasks
{
    "title": "",
    "description": "",
    "status": "INVALID_STATUS",
    "priority": "INVALID_PRIORITY", 
    "assigneeId": ""
}
```

**Response:**

```json
{
    "status": 400,
    "message": [
        "Title is required",
        "Description is required", 
        "Invalid task status. Valid values are: TO_DO, IN_PROGRESS, DONE",
        "Invalid task priority. Valid values are: LOW, MEDIUM, HIGH, URGENT",
        "Id of Assignee is required"
    ],
    "error": "Bad Request"
}
```

### 2. Partial Validation Failure

**Request:**

```json
POST /api/tasks
{
    "title": "Valid Title",
    "description": "Valid Description",
    "status": "INVALID_STATUS",
    "priority": "INVALID_PRIORITY",
    "assigneeId": "user-123"
}
```

**Response:**

```json
{
    "status": 400,
    "message": [
        "Invalid task status. Valid values are: TO_DO, IN_PROGRESS, DONE",
        "Invalid task priority. Valid values are: LOW, MEDIUM, HIGH, URGENT"
    ],
    "error": "Bad Request"
}
```

### 3. Single Error (Non-Validation)

**Request:**

```json
POST /api/tasks
{
    "title": "Valid Title",
    "description": "Valid Description", 
    "status": "TO_DO",
    "priority": "HIGH",
    "assigneeId": "non-existent-user"
}
```

**Response:**

```json
{
    "status": 400,
    "message": "User not found with id: non-existent-user",
    "error": "Bad Request"
}
```

## Implementation Details

### ErrorResponse Class

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int status;
    private Object message; // Can be String or List<String>
    private String error;
    
    // Convenience constructors for different message types
    public ErrorResponse(int status, String message, String error);
    public ErrorResponse(int status, List<String> messages, String error);
}
```

### GlobalExceptionHandler

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
    List<String> errorMessages = new ArrayList<>();
    
    ex.getBindingResult().getAllErrors().forEach(error -> {
        String errorMessage = error.getDefaultMessage();
        errorMessages.add(errorMessage);
    });

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        errorMessages,
        HttpStatus.BAD_REQUEST.getReasonPhrase()
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
}
```

## Validation Messages

### Task Status Validation

- **Valid values:** TO_DO, IN_PROGRESS, DONE (case-insensitive)
- **Error message:** "Invalid task status. Valid values are: TO_DO, IN_PROGRESS, DONE"

### Task Priority Validation  

- **Valid values:** LOW, MEDIUM, HIGH, URGENT (case-insensitive)
- **Error message:** "Invalid task priority. Valid values are: LOW, MEDIUM, HIGH, URGENT"

### Required Field Validation

- **Title:** "Title is required"
- **Description:** "Description is required"  
- **Assignee ID:** "Id of Assignee is required"
