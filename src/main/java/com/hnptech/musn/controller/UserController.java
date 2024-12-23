package com.hnptech.musn.controller;

import com.hnptech.musn.config.CustomUserDetails;
import com.hnptech.musn.entity.dto.UserDto;
import com.hnptech.musn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping
  public ResponseEntity<?> getUserByNickname(@RequestParam(defaultValue = "") String nickname) {
    //todo 페이지네이션
    return ResponseEntity.ok(userService.getUserByNickname(nickname));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails,@PathVariable Long userId) {
    return ResponseEntity.ok(new UserDto(userService.getUserById(userId)));
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/{userId}/friends/requests")
  public ResponseEntity<?> sendFriendRequest(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                             @PathVariable Long userId) {
    userService.sendFriendRequest(customUserDetails.getUser(), userId);
    return ResponseEntity.ok().build();
  }
}
