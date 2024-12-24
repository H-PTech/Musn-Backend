package com.hnptech.musn.entity.dto;

import com.hnptech.musn.entity.enums.FriendshipStatus;
import lombok.Getter;

@Getter
public class FriendshipUpdateRequest {
  private FriendshipStatus status;
}
