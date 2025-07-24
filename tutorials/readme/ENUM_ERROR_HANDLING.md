# Task Enum Validation - Error Handling

## ✅ Enhanced Error Messages

With the custom deserializers, you now get cleaner, more user-friendly error messages when invalid enum values are sent.

## API Error Examples

### ❌ Invalid Status Error

**Request:**

```json
POST /api/tasks
{
    "title": "Task 1999",
    "description": "Desc ...",
    "assigneeId": "557872ba-9459-4dea-9936-5e1443c37e7e",
    "status": "A"
}
```

**Response:**

```json
{
    "error": "Invalid task status: 'A'. Valid values are: TO_DO, IN_PROGRESS, DONE"
}
```

### ❌ Invalid Priority Error

**Request:**

```json
POST /api/tasks
{
    "title": "Task 2000",
    "description": "Another task",
    "assigneeId": "557872ba-9459-4dea-9936-5e1443c37e7e",
    "priority": "SUPER_HIGH"
}
```

**Response:**

```json
{
    "error": "Invalid task priority: 'SUPER_HIGH'. Valid values are: LOW, MEDIUM, HIGH, URGENT"
}
```

## ✅ Valid Enum Values

### TaskStatus

- `TO_DO` (default)
- `IN_PROGRESS`
- `DONE`

### TaskPriority

- `LOW`
- `MEDIUM` (default)
- `HIGH`
- `URGENT`

## ✅ Flexible Input Formats

The system accepts various formats (case-insensitive):

```json
{
    "status": "TO_DO",        // ✅ Exact match
    "status": "to_do",        // ✅ Lowercase
    "status": "To Do",        // ✅ Display name
    "priority": "HIGH",       // ✅ Exact match
    "priority": "high",       // ✅ Lowercase
    "priority": "High"        // ✅ Display name
}
```

## Implementation Details

### Custom Deserializers

- `TaskStatusDeserializer`: Handles TaskStatus enum deserialization
- `TaskPriorityDeserializer`: Handles TaskPriority enum deserialization
- Both provide clear error messages with valid values listed

### Benefits

1. **Clear Error Messages**: Users know exactly what went wrong
2. **Valid Values Listed**: No guessing what values are accepted
3. **Case Insensitive**: Flexible input handling
4. **Null Support**: Optional fields work properly
5. **Type Safety**: Compile-time enum validation maintained
