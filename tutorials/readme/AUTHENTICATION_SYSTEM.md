# Authentication System Documentation

## Overview

The authentication system provides JWT-based login functionality using username (email) and password.

## API Endpoints

### POST /api/auth/login

Authenticates a user and returns user information along with an access token.

**Request Body:**

```json
{
    "username": "user@example.com",
    "password": "password123"
}
```

**Successful Response (200 OK):**

```json
{
    "user": {
        "id": "user-uuid-123",
        "name": "John Doe",
        "email": "user@example.com",
        "roles": [],
        "createdAt": "2025-07-20T10:30:00",
        "updatedAt": "2025-07-20T11:45:00",
        "createdBy": null,
        "updatedBy": null
    },
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJ1c2VyLXV1aWQtMTIzIiwic3ViIjoidXNlckBleGFtcGxlLmNvbSIsImlhdCI6MTY4NzI0ODYwMCwiZXhwIjoxNjg3MzM1MDAwfQ.signature",
    "tokenType": "Bearer",
    "expiresIn": 86400
}
```

**Error Response (401 Unauthorized):**

```json
{
    "status": 401,
    "message": "Invalid username or password",
    "error": "Unauthorized"
}
```

**Validation Error Response (400 Bad Request):**

```json
{
    "status": 400,
    "message": [
        "Username is required",
        "Password is required"
    ],
    "error": "Bad Request"
}
```

## Usage Examples

### 1. Login with cURL

```bash
curl -X POST http://localhost:8888/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user@example.com",
    "password": "password123"
  }'
```

### 2. Login with JavaScript (fetch)

```javascript
const loginUser = async (username, password) => {
    try {
        const response = await fetch('http://localhost:8888/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });

        if (response.ok) {
            const authData = await response.json();
            console.log('Login successful:', authData);
            
            // Store token for future requests
            localStorage.setItem('accessToken', authData.accessToken);
            
            return authData;
        } else {
            const error = await response.json();
            console.error('Login failed:', error);
            throw new Error(error.message);
        }
    } catch (error) {
        console.error('Network error:', error);
        throw error;
    }
};

// Usage
loginUser('user@example.com', 'password123')
    .then(authData => {
        console.log('User:', authData.user);
        console.log('Token:', authData.accessToken);
    })
    .catch(error => {
        console.error('Login error:', error.message);
    });
```

### 3. Using the Access Token

```javascript
// Example of using the token in subsequent requests
const makeAuthenticatedRequest = async (url, options = {}) => {
    const token = localStorage.getItem('accessToken');
    
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };
    
    if (token) {
        headers.Authorization = `Bearer ${token}`;
    }
    
    return fetch(url, {
        ...options,
        headers
    });
};

// Usage
makeAuthenticatedRequest('http://localhost:8888/api/tasks', {
    method: 'GET'
})
.then(response => response.json())
.then(data => console.log('Tasks:', data));
```

## Security Configuration

### JWT Token Properties

- **Secret Key**: Configurable via `app.jwt.secret` property
- **Expiration**: Configurable via `app.jwt.expiration` property (in seconds)
- **Default Expiration**: 24 hours (86400 seconds)

### Password Security

- Passwords are stored as plain text in the database (for development/learning purposes)
- Password validation compares raw input with stored password
- **Note**: In production, you should always use password hashing for security

### Endpoint Security

- `/api/auth/**` - Public (no authentication required)
- `/api/users` - Public (for user registration)  
- All other endpoints - Authentication required

## Implementation Details

### Key Components

1. **AuthController** - Handles login requests
2. **AuthService** - Business logic for authentication
3. **JwtService** - JWT token generation and validation
4. **SecurityConfig** - Spring Security configuration
5. **LoginRequestDto** - Login request validation
6. **AuthResponseDto** - Authentication response structure

### Database Requirements

The system expects a `users` table with the following structure:

- `id` - UUID primary key
- `name` - User display name
- `email` - Unique email address (used as username)
- `password` - BCrypt-hashed password
- `created_at` - Creation timestamp
- `updated_at` - Last update timestamp
- `created_by` - Created by user ID
- `updated_by` - Updated by user ID

### Testing Authentication

1. **Create a User First:**

```bash
curl -X POST http://localhost:8888/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "Password123"
  }'
```

2. **Then Login:**

```bash
curl -X POST http://localhost:8888/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test@example.com",
    "password": "Password123"
  }'
```

## Error Handling

The authentication system provides comprehensive error handling:

- **Invalid Credentials**: Returns 401 Unauthorized
- **Validation Errors**: Returns 400 Bad Request with field-specific errors
- **Missing Fields**: Returns validation error messages
- **Server Errors**: Returns 500 Internal Server Error

All error responses follow the consistent error response format established in the application.
