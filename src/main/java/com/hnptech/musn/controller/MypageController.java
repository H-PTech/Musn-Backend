package com.hnptech.musn.controller;

import com.hnptech.musn.config.CustomUserDetails;
import com.hnptech.musn.entity.dto.MypageUpdateNicknameReqDto;
import com.hnptech.musn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/mypage")
@RequiredArgsConstructor
public class MypageController {
  private final UserService userService;

  @GetMapping
  public ResponseEntity<?> getUserById(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
    return ResponseEntity.ok(customUserDetails.getUser().getId());
  }

  @PatchMapping
  public ResponseEntity<?> updateNickname(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                          @RequestBody MypageUpdateNicknameReqDto dto) {
    userService.updateNickName(customUserDetails.getUser(), dto.getNickname());
    return ResponseEntity.ok().build();
  }
}
