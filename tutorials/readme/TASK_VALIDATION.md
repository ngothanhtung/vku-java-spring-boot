# Task Enum Validation Implementation

## Overview

This implementation provides robust validation for task status and priority fields using custom validation annotations that ensure only valid enum values are accepted.

## Valid Values

### TaskStatus

- `TO_DO` (default)
- `IN_PROGRESS`
- `DONE`

### TaskPriority

- `LOW`
- `MEDIUM` (default)
- `HIGH`
- `URGENT`

## API Examples

### ✅ Valid Request Examples

#### Create Task

```json
POST /api/tasks
{
  "title": "Implement user authentication",
  "description": "Add JWT-based authentication system",
  "status": "TO_DO",
  "priority": "HIGH",
  "assigneeId": "user-123"
}
```

#### Update Task (Case-Insensitive)

```json
PATCH /api/tasks/task-456
{
  "status": "in_progress",
  "priority": "urgent"
}
```

#### Create Task with Defaults

```json
POST /api/tasks
{
  "title": "Review code",
  "description": "Review pull request #42",
  "assigneeId": "user-789"
}
// status defaults to "TO_DO", priority defaults to "MEDIUM"
```

### ❌ Invalid Request Examples

#### Invalid Status

```json
POST /api/tasks
{
  "title": "Test task",
  "description": "Test description",
  "status": "INVALID_STATUS",
  "assigneeId": "user-123"
}
// Returns 400 Bad Request with error:
// "Invalid task status. Valid values are: TO_DO, IN_PROGRESS, DONE"
```

#### Invalid Priority

```json
PATCH /api/tasks/task-456
{
  "priority": "SUPER_HIGH"
}
// Returns 400 Bad Request with error:
// "Invalid task priority. Valid values are: LOW, MEDIUM, HIGH, URGENT"
```

## Implementation Details

### Custom Validation Annotations

- `@ValidTaskStatus`: Validates status field against TaskStatus enum
- `@ValidTaskPriority`: Validates priority field against TaskPriority enum
- Both support `allowNull = true` for optional fields

### Features

- **Type Safety**: Compile-time validation for internal enum usage
- **Runtime Validation**: Request validation with clear error messages
- **Case Insensitive**: Accepts "TO_DO", "to_do", "To Do" formats
- **Null Support**: Optional fields can be null for defaults
- **Comprehensive Testing**: Full test coverage for validation scenarios

### Benefits

1. **Prevents Invalid Data**: No invalid status/priority values can enter the system
2. **User Friendly**: Clear error messages for invalid inputs
3. **Flexible Input**: Accepts various case formats
4. **Maintainable**: Easy to add new enum values
5. **Performance**: Fast enum-based validation with caching
