package com.hnptech.musn.service;

import com.hnptech.musn.entity.User;
import com.hnptech.musn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public void updateNickName(User user, String nickname) {
    user.setNickname(nickname);
    userRepository.save(user);
  }
}
