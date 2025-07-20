package vku.apiservice.tutorials.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for JWT authentication with RBAC
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(exceptions -> exceptions
            .accessDeniedHandler(customAccessDeniedHandler)
            .authenticationEntryPoint(customAuthenticationEntryPoint))
        .authorizeHttpRequests(authz -> authz
            // Public endpoints - no authentication required
            .requestMatchers("/api/auth/**").permitAll()

            // Admin-only endpoints
            .requestMatchers("/api/users/**").hasRole(RoleConstants.ADMIN)
            .requestMatchers("/api/roles/**").hasRole(RoleConstants.ADMIN)

            // Project management - Admin and Manager can manage, Users can view
            .requestMatchers(HttpMethod.POST, "/api/projects/**").hasAnyRole(RoleConstants.ADMIN, RoleConstants.MANAGER)
            .requestMatchers(HttpMethod.PUT, "/api/projects/**").hasAnyRole(RoleConstants.ADMIN, RoleConstants.MANAGER)
            .requestMatchers(HttpMethod.DELETE, "/api/projects/**")
            .hasAnyRole(RoleConstants.ADMIN, RoleConstants.MANAGER)
            .requestMatchers(HttpMethod.GET, "/api/projects/**").hasAnyRole(RoleConstants.MANAGER, RoleConstants.USER)

            // Task management - All authenticated users can access tasks
            .requestMatchers("/api/tasks/**").hasAnyRole(RoleConstants.ADMIN, RoleConstants.MANAGER, RoleConstants.USER)

            // Any other request requires authentication
            .anyRequest().authenticated())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
