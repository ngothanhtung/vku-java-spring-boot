package vku.apiservice.tutorials.config;

// Example usage of PreAuthorizeUtil for custom combinations:
// 
// For single role:
// @PreAuthorize(PreAuthorizeUtil.ADMIN_ONLY)
//
// For multiple roles:  
// @PreAuthorize(PreAuthorizeUtil.ADMIN_OR_MANAGER)
// @PreAuthorize(PreAuthorizeUtil.ALL_AUTHENTICATED)
//
// For custom combinations:
// @PreAuthorize("hasAnyRole('" + RoleConstants.ADMIN + "', '" + RoleConstants.USER + "')")
// Or using the utility method:
// @PreAuthorize(PreAuthorizeUtil.hasAnyRole(RoleConstants.ADMIN, RoleConstants.USER))
//
// For role + ownership check:
// @PreAuthorize(PreAuthorizeUtil.ADMIN_OR_MANAGER_OR_OWNER)
// @PreAuthorize(PreAuthorizeUtil.ADMIN_ONLY + " or @someService.isOwner(#id, authentication.name)")

/**
 * Example of different authorization patterns
 */
public class AuthorizationExamples {
  // This class is just for documentation - not meant to be instantiated
}