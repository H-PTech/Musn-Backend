package com.hnptech.musn.service;

import com.hnptech.musn.exception.BadRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GoogleService {

  private final RestTemplate restTemplate = new RestTemplate();

  public Map<String, Object> getUserInfo(String accessToken) {
    String userInfoUrl = "https://www.googleapis.com/userinfo/v2/me";

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + accessToken);

    HttpEntity<Void> request = new HttpEntity<>(headers);

    try {
      ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);
      return response.getBody();
    } catch (HttpClientErrorException e) {
      throw new BadRequestException("this access token does not exist");
    }
  }
}