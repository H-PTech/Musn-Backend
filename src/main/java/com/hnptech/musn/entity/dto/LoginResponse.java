package com.hnptech.musn.entity.dto;

import com.hnptech.musn.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse extends UserDto{
  private String accessToken;
  private String refreshToken;

  public LoginResponse(User user, String accessToken, String refreshToken) {
    super(user);
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
