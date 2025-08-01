package vku.apiservice.tutorials.domain.security.events;

import lombok.Value;

@Value
public class RoleAssignedEvent {
    private final String userId;
    private final String roleId;
}