package vku.apiservice.tutorials.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vku.apiservice.tutorials.domain.security.entities.User;
import vku.apiservice.tutorials.domain.security.entities.UserRole;
import vku.apiservice.tutorials.domain.security.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    List<GrantedAuthority> authorities = new ArrayList<>();

    // Add user roles as authorities with both ROLE_ prefix and without prefix for
    // flexibility
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