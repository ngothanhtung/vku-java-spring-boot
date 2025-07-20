package vku.apiservice.tutorials.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class UserDtoTest {

  @Test
  void testUserDtoBuilderPattern() {
    LocalDateTime now = LocalDateTime.now();
    List<RoleDto> roles = new ArrayList<>();
    roles.add(new RoleDto("role-1", "Admin"));
    roles.add(new RoleDto("role-2", "User"));

    UserDto userDto = UserDto.builder()
        .id("user-123")
        .name("John Doe")
        .email("john.doe@example.com")
        .roles(roles)
        .createdAt(now)
        .updatedAt(now)
        .createdBy("system")
        .updatedBy("system")
        .build();

    assertNotNull(userDto);
    assertEquals("user-123", userDto.getId());
    assertEquals("John Doe", userDto.getName());
    assertEquals("john.doe@example.com", userDto.getEmail());
    assertEquals(2, userDto.getRoles().size());
    assertEquals("Admin", userDto.getRoles().get(0).getName());
    assertEquals("User", userDto.getRoles().get(1).getName());
    assertEquals(now, userDto.getCreatedAt());
    assertEquals(now, userDto.getUpdatedAt());
    assertEquals("system", userDto.getCreatedBy());
    assertEquals("system", userDto.getUpdatedBy());
  }

  @Test
  void testUserDtoConstructor() {
    List<RoleDto> roles = new ArrayList<>();
    roles.add(new RoleDto("role-1", "Admin"));

    UserDto userDto = new UserDto();
    userDto.setId("user-456");
    userDto.setName("Jane Smith");
    userDto.setEmail("jane.smith@example.com");
    userDto.setRoles(roles);

    assertNotNull(userDto);
    assertEquals("user-456", userDto.getId());
    assertEquals("Jane Smith", userDto.getName());
    assertEquals("jane.smith@example.com", userDto.getEmail());
    assertEquals(1, userDto.getRoles().size());
    assertEquals("Admin", userDto.getRoles().get(0).getName());
  }
}
