package com.hnptech.musn.service;

import com.hnptech.musn.config.CustomOAuth2UserService;
import com.hnptech.musn.config.CustomUserDetails;
import com.hnptech.musn.entity.User;
import com.hnptech.musn.entity.dto.LoginResponse;
import com.hnptech.musn.exception.BadRequestException;
import com.hnptech.musn.exception.UnauthorizedException;
import com.hnptech.musn.repository.UserRepository;
import com.hnptech.musn.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final KakaoService kakaoService;
  private final GoogleService googleService;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  private enum Provider {
    kakao, google
  }

  public LoginResponse login(String provider, String socialAccessToken) {
    try {
      Provider.valueOf(provider);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("provider not supported");
    }

    Map<String, Object> userInfo = Map.of();
    String providerId;

    switch (provider) {
      case "kakao" -> userInfo = kakaoService.getUserInfo(socialAccessToken);
      case "google" -> userInfo = googleService.getUserInfo(socialAccessToken);
    }

    providerId = String.valueOf(userInfo.get("id"));
    User user = customOAuth2UserService.getOrSave(provider, providerId);
    OAuth2User oAuth2User = new CustomUserDetails(user);
    Authentication authentication = new UsernamePasswordAuthenticationToken(oAuth2User, "", oAuth2User.getAuthorities());

    String accessToken = jwtUtil.createAccessToken(authentication);
    String refreshToken = jwtUtil.createRefreshToken(authentication);
    jwtUtil.saveRefreshToken(authentication.getName(), refreshToken);

    return new LoginResponse(user, accessToken, refreshToken);
  }

  public Map<String, Object> refresh(String refreshToken) {
    if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
      throw new UnauthorizedException("Invalid refresh token");
    }

    String token = refreshToken.substring(7);

    if (!jwtUtil.validateToken(token)) {
      throw new UnauthorizedException("Invalid refresh token");
    }

    User user = userRepository.findById(jwtUtil.getId(token)).orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

    if (!jwtUtil.getRefreshToken(user.getId().toString()).equals(token)) {
      throw new UnauthorizedException("Invalid refresh token");
    }

    OAuth2User oAuth2User = new CustomUserDetails(user);
    Authentication authentication = new UsernamePasswordAuthenticationToken(oAuth2User, "", oAuth2User.getAuthorities());
    String newAccessToken = jwtUtil.createAccessToken(authentication);
    String newRefreshToken = jwtUtil.createRefreshToken(authentication);
    jwtUtil.saveRefreshToken(authentication.getName(), newRefreshToken);

    return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
  }

  public void validate(String token) {
    if (token == null || !token.startsWith("Bearer ")) {
      throw new UnauthorizedException("Invalid refresh token");
    }

    if (!jwtUtil.validateToken(token.substring(7))) {
      throw new UnauthorizedException("Invalid refresh token");
    }
  }
}
