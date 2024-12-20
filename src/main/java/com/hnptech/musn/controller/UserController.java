package com.hnptech.musn.controller;


import com.hnptech.musn.config.CustomUserDetails;
import com.hnptech.musn.entity.User;
import com.hnptech.musn.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {
  UserService userService;

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/test")
  public String getAllUsers(@AuthenticationPrincipal CustomUserDetails user) {
    System.out.println(SecurityContextHolder.getContext().getAuthentication());

    System.out.println(user);
    return "dddd";
  }
}
