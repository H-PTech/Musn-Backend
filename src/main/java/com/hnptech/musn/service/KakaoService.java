package com.hnptech.musn.service;

import com.hnptech.musn.exception.BadRequestException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KakaoService {

  //todo restTemplate config 만들어서 custom handler 적용
  private final RestTemplate restTemplate = new RestTemplate();

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

