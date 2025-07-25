package vku.apiservice.tutorials.domain.security.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserIdsRequestDto {
  private List<String> userIds;

}
