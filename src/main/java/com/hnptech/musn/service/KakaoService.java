package com.hnptech.musn.service;

import com.hnptech.musn.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Service
public class KakaoService {

  @Value("${social.provider.kakao.app-id}")
  private Integer kakaoAppId;

  //todo restTemplate config 만들어서 custom handler 적용
  private final RestTemplate restTemplate = new RestTemplate();

  public void validateTokenInfo(Integer expiresIn, Integer appId) {
    if (expiresIn > 0 && Objects.equals(kakaoAppId, appId)) {
      return;
    }
    throw new BadRequestException("유효하지 않은 토큰입니다.");
  }

  public Map<String, Object> getAccessTokenInfo(String accessToken) {
    String accessTokenInfoUrl = "https://kapi.kakao.com/v1/user/access_token_info";

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + accessToken);

    HttpEntity<Void> request = new HttpEntity<>(headers);
    try {
      ResponseEntity<Map> response = restTemplate.exchange(accessTokenInfoUrl, HttpMethod.GET, request, Map.class);
      return response.getBody();
    } catch (HttpClientErrorException e) {
      Map body = e.getResponseBodyAs(Map.class);
      throw new BadRequestException(body.get("msg").toString());
    }
  }

  public Map<String, Object> getUserInfo(String accessToken) {
    String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + accessToken);

    HttpEntity<Void> request = new HttpEntity<>(headers);

    try {
      ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);
      return response.getBody();
    } catch (HttpClientErrorException e) {
      Map body = e.getResponseBodyAs(Map.class);
      throw new BadRequestException(body.get("msg").toString());
    }
  }
}

