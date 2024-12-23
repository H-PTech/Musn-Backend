package com.hnptech.musn.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hnptech.musn.entity.User;
import com.hnptech.musn.entity.enums.StreamingApp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
  private Long id;

  private String nickname;

  private StreamingApp streamingApp;

  @JsonProperty("isPush")
  private boolean isPush;

  public UserDto(User user) {
    this.id = user.getId();
    this.nickname = user.getNickname();
    this.streamingApp = user.getStreamingApp();
    this.isPush = user.isPush();
  }
}
