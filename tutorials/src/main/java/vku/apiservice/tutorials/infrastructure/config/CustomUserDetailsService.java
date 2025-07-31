package vku.apiservice.tutorials.infrastructure.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vku.apiservice.tutorials.domain.security.entities.User;
import vku.apiservice.tutorials.domain.security.entities.UserRole;
import vku.apiservice.tutorials.infrastructure.persistence.jpa.security.UserJpaRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserJpaRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    List<GrantedAuthority> authorities = new ArrayList<>();

    // Add user roles as authorities with both ROLE_ prefix and without prefix for
    // flexibility

    /*
     * Bạn cần cả hai SimpleGrantedAuthority (một với tiền tố ROLE_, một không có
     * tiền tố) vì:
     * 
     * Spring Security mặc định: Spring Security yêu cầu quyền (authority) dùng cho
     * phân quyền URL hoặc annotation như @Secured, @RolesAllowed phải có tiền tố
     * ROLE_. Ví dụ: ROLE_ADMIN.
     * 
     * @PreAuthorize hoặc custom check: Khi dùng annotation
     * như @PreAuthorize("hasAuthority('ADMIN')") hoặc bạn muốn kiểm tra quyền mà
     * không cần tiền tố, bạn sẽ cần authority không có ROLE_.
     * 
     */
    if (user.getUserRoles() != null) {
      for (UserRole userRole : user.getUserRoles()) {
        if (userRole.isEnabled()) { // Only include enabled roles
          String roleCode = userRole.getRole().getCode();

          // Add a role with ROLE_ prefix (Spring Security standard)
          authorities.add(new SimpleGrantedAuthority("ROLE_" + roleCode.toUpperCase()));

          // Add a role without the prefix for @PreAuthorize usage
          authorities.add(new SimpleGrantedAuthority(roleCode.toUpperCase()));
        }
      }
    }

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password(user.getPassword() != null ? user.getPassword() : "")
        .authorities(authorities)
        .accountExpired(false)
        .accountLocked(false)
        .credentialsExpired(false)
        .disabled(false)
        .build();
  }
}