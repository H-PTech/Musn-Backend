package com.hnptech.musn.service;

import com.hnptech.musn.entity.User;
import com.hnptech.musn.exception.NotFoundException;
import com.hnptech.musn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public User getUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다."));
  }
}
