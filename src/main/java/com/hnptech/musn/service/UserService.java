package com.hnptech.musn.service;

import com.hnptech.musn.entity.User;
import com.hnptech.musn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public void updateNickName(User user, String nickname) {
    userRepository.findByNickname(nickname)
        .ifPresent(u -> {throw new DataIntegrityViolationException("이미 사용중인 닉네임입니다.");});

    user.setNickname(nickname);
    userRepository.save(user);
  }
}
