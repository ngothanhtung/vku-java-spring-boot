package vku.apiservice.tutorials.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Utility class for accessing current user information from SecurityContext
 */
@Component
public class SecurityUtils {

  /**
   * Get the current authenticated user's email/username
   */
  public static String getCurrentUserEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      return userDetails.getUsername();
    }
    return null;
  }

  /**
   * Check if current user has a specific role
   */
  public static boolean hasRole(String role) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      return authentication.getAuthorities().stream()
          .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role) ||
              authority.getAuthority().equals(role));
    }
    return false;
  }

  /**
   * Check if current user has any of the specified roles
   */
  public static boolean hasAnyRole(String... roles) {
    for (String role : roles) {
      if (hasRole(role)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if user is authenticated
   */
  public static boolean isAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null && authentication.isAuthenticated() &&
        !"anonymousUser".equals(authentication.getPrincipal());
  }
}
