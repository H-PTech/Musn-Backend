package com.hnptech.musn.entity.dto;

import lombok.Getter;

@Getter
public class LoginRequest {
  private String provider;
  private String token;
}
