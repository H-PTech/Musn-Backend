package com.hnptech.musn.controller;

import com.hnptech.musn.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/login/{provider}")
  public ResponseEntity<?> login(@PathVariable("provider") String provider,
                                 @RequestHeader("Authorization") String socialAccessToken) {
    return ResponseEntity.ok(authService.login(provider, socialAccessToken));
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String refreshToken) {
    return ResponseEntity.ok(authService.refresh(refreshToken));
  }

  @GetMapping("/validate")
  public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
    authService.validate(token);
    return ResponseEntity.ok().build();
  }
}
