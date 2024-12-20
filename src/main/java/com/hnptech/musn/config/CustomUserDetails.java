package com.hnptech.musn.config;

import com.hnptech.musn.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
@ToString
@RequiredArgsConstructor
public class CustomUserDetails implements OAuth2User {

  private final User user;
  private final Map<String, Object> attributes;

  public CustomUserDetails(User user) {
    this.user = user;
    attributes = Map.of();
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    return authorities;
  }

  @Override
  public String getName() {
    return user.getId().toString();
  }
}