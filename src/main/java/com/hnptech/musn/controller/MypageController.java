package com.hnptech.musn.controller;

import com.hnptech.musn.config.CustomUserDetails;
import com.hnptech.musn.entity.dto.FriendshipUpdateRequest;
import com.hnptech.musn.entity.dto.NicknameUpdateRequest;
import com.hnptech.musn.entity.dto.PushUpdateRequest;
import com.hnptech.musn.entity.dto.UserDto;
import com.hnptech.musn.entity.enums.FriendshipStatus;
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
    return ResponseEntity.ok(new UserDto(customUserDetails.getUser()));
  }

  @PatchMapping
  public ResponseEntity<?> updateNickname(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                          @RequestBody NicknameUpdateRequest dto) {
    userService.updateNickName(customUserDetails.getUser(), dto.getNickname());
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/push")
  public ResponseEntity<?> updatePushStatus(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                            @RequestBody PushUpdateRequest dto) {
    userService.updatePushStatus(customUserDetails.getUser(), dto.isPush());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/friends")
  public ResponseEntity<?> getFriends(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
    return ResponseEntity.ok(userService.findByUserAndStatus(customUserDetails.getUser(), FriendshipStatus.ACCEPTED));
  }

  @GetMapping("/friends/requests")
  public ResponseEntity<?> getFriendRequests(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
    return ResponseEntity.ok(userService.findByUserAndStatus(customUserDetails.getUser(), FriendshipStatus.REQUESTED));
  }

  @PatchMapping("/friends/requests/{requestId}")
  public ResponseEntity<?> updateFriendshipStatus(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                  @PathVariable Long requestId,
                                                  @RequestBody FriendshipUpdateRequest dto) {
    userService.updateFriendshipStatus(customUserDetails.getUser(), requestId, dto.getStatus());
    return ResponseEntity.ok().build();
  }
}
