package vku.apiservice.tutorials.config;

/**
 * Constants for role-based access control
 */
public final class RoleConstants {

  // Role codes (should match your database)
  public static final String ADMIN = "ADMINISTRATORS";
  public static final String MANAGER = "MANAGERS";
  public static final String USER = "USERS";

  // Role authorities for Spring Security
  public static final String ROLE_ADMIN = "ROLE_" + ADMIN;
  public static final String ROLE_MANAGER = "ROLE_" + MANAGER;
  public static final String ROLE_USER = "ROLE_" + USER;

  private RoleConstants() {
    // Utility class - prevent instantiation
  }
}
