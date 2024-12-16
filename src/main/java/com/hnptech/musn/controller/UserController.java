package com.hnptech.musn.controller;


import com.hnptech.musn.entity.User;
import com.hnptech.musn.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {
    UserService userService;
}
