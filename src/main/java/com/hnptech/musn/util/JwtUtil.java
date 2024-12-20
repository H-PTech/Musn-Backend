package com.hnptech.musn.util;

import com.hnptech.musn.config.CustomOAuth2UserService;
import com.hnptech.musn.config.CustomUserDetails;
import com.hnptech.musn.entity.User;
import com.hnptech.musn.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

  private enum TokenType {
    ACCESS, REFRESH
  }

  @Value("${jwt.token.secret-key}")
  private String SECRET_KEY;

  @Value("${jwt.token.access-expire-time}")
  private long ACCESS_TOKEN_EXPIRE_TIME;

  @Value("${jwt.token.refresh-expire-time}")
  private long REFRESH_TOKEN_EXPIRE_TIME;

  private final RedisUtil redisUtil;
  private final CustomOAuth2UserService customOAuth2UserService;

  public String createAccessToken(Authentication authentication) {
    return createToken(authentication, TokenType.ACCESS, ACCESS_TOKEN_EXPIRE_TIME);
  }

  public String createRefreshToken(Authentication authentication) {
    return createToken(authentication, TokenType.REFRESH, REFRESH_TOKEN_EXPIRE_TIME);
  }

  private String createToken(Authentication authentication, TokenType tokenType, long expireTime) {
    User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
    return Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setSubject(String.valueOf(user.getId()))
        .claim("nickname", user.getNickname())
        .claim("token_type", tokenType)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expireTime))
        .signWith(getSigningKey())
        .compact();
  }

  private Key getSigningKey() {
    byte[] keyBytes = SECRET_KEY.getBytes();
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public boolean validateToken(String token) {
    if (token == null) return false;

    try {
      return Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token)
          .getBody()
          .getExpiration()
          .after(new Date());
    } catch (ExpiredJwtException e) {
      throw new UnauthorizedException("만료된 토큰입니다.");
    } catch (Exception e) {
      throw new UnauthorizedException("유효하지 않은 토큰입니다.");
    }
  }

  private Claims parseClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public int getId(String token) {
    return Integer.parseInt(parseClaims(token).getSubject());
  }

  public void saveRefreshToken(String userId, String refreshToken) {
    redisUtil.setex("refreshToken:" + userId, refreshToken, REFRESH_TOKEN_EXPIRE_TIME);
  }

  public String getRefreshToken(String userId) {
    return (String) redisUtil.get("refreshToken:" + userId);
  }

  public void deleteRefreshToken(String userId) {
    redisUtil.delete("refreshToken:" + userId);
  }
}