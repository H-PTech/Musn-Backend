package com.hnptech.musn.filter;

import com.hnptech.musn.config.CustomUserDetails;
import com.hnptech.musn.entity.User;
import com.hnptech.musn.exception.UnauthorizedException;
import com.hnptech.musn.repository.UserRepository;
import com.hnptech.musn.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String bearerToken = request.getHeader("Authorization");

    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      String token = bearerToken.substring(7);
      User user;

      if (jwtUtil.validateToken(token)) {
        user = userRepository.findByIdAndDeleted(jwtUtil.getId(token), false).orElseThrow(() -> new UnauthorizedException("Invalid token"));
        OAuth2User oAuth2User = new CustomUserDetails(user);
//        Authentication authentication = new OAuth2AuthenticationToken(oAuth2User, oAuth2User.getAuthorities(), user.getProvider());
        Authentication authentication = new UsernamePasswordAuthenticationToken(oAuth2User, "", oAuth2User.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }

    filterChain.doFilter(request, response);
  }
}
