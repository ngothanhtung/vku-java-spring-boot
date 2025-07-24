package vku.apiservice.tutorials.infrastructure.config;

/**
 * Utility class to generate PreAuthorize expressions for roles
 */
public final class PreAuthorizeUtil {

  private PreAuthorizeUtil() {
    // Utility class - prevent instantiation
  }

  // Predefined common expressions as compile-time constants
  public static final String ADMIN_ONLY = "hasRole('" + RoleConstants.ADMIN + "')";
  public static final String MANAGER_ONLY = "hasRole('" + RoleConstants.MANAGER + "')";
  public static final String USER_ONLY = "hasRole('" + RoleConstants.USER + "')";

  public static final String ADMIN_OR_MANAGER = "hasAnyRole('" + RoleConstants.ADMIN + "', '" + RoleConstants.MANAGER
      + "')";
  public static final String ADMIN_OR_USER = "hasAnyRole('" + RoleConstants.ADMIN + "', '" + RoleConstants.USER + "')";
  public static final String MANAGER_OR_USER = "hasAnyRole('" + RoleConstants.MANAGER + "', '" + RoleConstants.USER
      + "')";

  // Allow any authenticated user with valid JWT (any role or no role)
  public static final String ALL_AUTHENTICATED = "isAuthenticated()";

  // Specific roles only
  public static final String ALL_WITH_ROLES = "hasAnyRole('" + RoleConstants.ADMIN + "', '" + RoleConstants.MANAGER
      + "', '" + RoleConstants.USER + "')";

  // Common patterns with user ownership
  public static final String ADMIN_OR_MANAGER_OR_OWNER = ADMIN_OR_MANAGER
      + " or @userService.isCurrentUser(#id, authentication.name)";
  public static final String ADMIN_OR_TASK_OWNER = ADMIN_ONLY
      + " or @taskService.isTaskOwner(#assigneeId, authentication.name)";

  /**
   * Generate hasRole expression for a single role
   * 
   * @param role the role constant
   * @return PreAuthorize expression string
   */
  public static String hasRole(String role) {
    return "hasRole('" + role + "')";
  }

  /**
   * Generate hasAnyRole expression for multiple roles
   * 
   * @param roles the role constants
   * @return PreAuthorize expression string
   */
  public static String hasAnyRole(String... roles) {
    if (roles.length == 1) {
      return hasRole(roles[0]);
    }

    StringBuilder sb = new StringBuilder("hasAnyRole(");
    for (int i = 0; i < roles.length; i++) {
      sb.append("'").append(roles[i]).append("'");
      if (i < roles.length - 1) {
        sb.append(", ");
      }
    }
    sb.append(")");
    return sb.toString();
  }
}