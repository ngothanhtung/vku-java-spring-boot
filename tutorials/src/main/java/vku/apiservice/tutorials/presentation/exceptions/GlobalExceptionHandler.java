package vku.apiservice.tutorials.presentation.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorResponse> handleHttpException(HttpException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatus().value(), List.of(ex.getMessage()),
                ex.getStatus().getReasonPhrase());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errorMessages = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            String fieldName = ((FieldError) error).getField();
            errorMessages.add(fieldName + ": " + errorMessage);

        });

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errorMessages,
                HttpStatus.BAD_REQUEST.getReasonPhrase());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", 403);
        errorResponse.put("error", "Forbidden");
        errorResponse.put("messages", List.of("Access denied: You don't have permission to access this resource"));

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", 401);
        errorResponse.put("error", "Unauthorized");
        errorResponse.put("messages", List.of("Authentication failed: " + ex.getMessage()));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}

// README
// This class handles global exceptions for the application.
// It intercepts exceptions thrown by controllers
// and provides a consistent error and response format.
