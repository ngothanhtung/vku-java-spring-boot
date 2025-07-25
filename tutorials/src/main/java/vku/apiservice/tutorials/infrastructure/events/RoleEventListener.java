package vku.apiservice.tutorials.infrastructure.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import vku.apiservice.tutorials.domain.security.events.RoleAssignedEvent;

@Component
public class RoleEventListener {
  private static final Logger logger = LoggerFactory.getLogger(RoleEventListener.class);

  @EventListener
  public void handleRoleAssignedEvent(RoleAssignedEvent event) {
    logger.info("🔥 Role assigned: userId={}, roleId={}", event.getUserId(), event.getRoleId());
    // Xử lý logic, ví dụ: gửi email thông báo, cập nhật cache, hoặc gọi API khác
  }
}