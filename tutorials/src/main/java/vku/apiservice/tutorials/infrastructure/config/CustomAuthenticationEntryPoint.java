package vku.apiservice.tutorials.infrastructure.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    // Create custom error response for 401 Unauthorized
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("status", 401);
    errorResponse.put("error", "Unauthorized");
    errorResponse.put("messages", List.of("Authentication required: Please provide a valid access token"));

    // Write JSON response
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}
// README
// This class implements the AuthenticationEntryPoint interface to handle
// unauthorized access attempts.
// It returns a custom JSON response with a 401 status code when authentication
// fails.
// It is used in the security filter chain to provide a consistent error
// response format for unauthorized requests.
// The response includes a status code, error message, and a list of messages
// indicating the authentication requirements.
// This allows clients to understand the nature of the authentication failure
// and take appropriate action.
// The ObjectMapper is used to convert the error response map into a JSON string
// for the HTTP response.
// The @Component annotation registers this class as a Spring bean, making it
// available for dependency injection in the security configuration.
// The commence method is invoked whenever an authentication error occurs,
// allowing for centralized error handling in the application.
// The method sets the response content type to "application/json" and writes
// the error response to the response body.
// This approach ensures that all unauthorized access attempts receive a
// consistent and informative error response,
// improving the overall user experience and making it easier for clients to
// handle authentication errors programmatically.