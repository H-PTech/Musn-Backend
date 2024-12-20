package com.hnptech.musn.handler;

import com.hnptech.musn.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Component
@RequiredArgsConstructor
public class OAuth2MemberSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtUtil jwtUtil;

  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

    System.out.println(authentication);
    String accessToken = jwtUtil.createAccessToken(authentication);
    String refreshToken = jwtUtil.createRefreshToken(authentication);
    jwtUtil.saveRefreshToken(authentication.getName(), refreshToken);

    queryParams.add("accessToken", accessToken);
    queryParams.add("refreshToken", refreshToken);

//    URI uri = UriComponentsBuilder
//        .fromUriString("http://localhost:3000")
//        .queryParams(queryParams)
//        .build()
//        .toUri();
    response.setContentType("application/json");
    response.getWriter().write("{\"accessToken\":\"" + accessToken + "\",\"refreshToken\":\"" + refreshToken + "\"}");
//    getRedirectStrategy().sendRedirect(request, response, uri.toString());
  }
}
