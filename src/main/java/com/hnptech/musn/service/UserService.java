package com.hnptech.musn.service;

import com.hnptech.musn.entity.Friendship;
import com.hnptech.musn.entity.User;
import com.hnptech.musn.entity.dto.UserDto;
import com.hnptech.musn.entity.enums.FriendshipStatus;
import com.hnptech.musn.exception.NotFoundException;
import com.hnptech.musn.repository.FriendshipRepository;
import com.hnptech.musn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final FriendshipRepository friendshipRepository;

  public void updateNickName(User user, String nickname) {
    userRepository.findByNickname(nickname)
        .ifPresent(u -> {throw new DataIntegrityViolationException("이미 사용중인 닉네임입니다.");});

    user.setNickname(nickname);
    userRepository.save(user);
  }

  public void updatePushStatus(User user, boolean isPush) {
    user.setPush(isPush);
    userRepository.save(user);
  }

  public List<UserDto> getUserByNickname(String Nickname) {
    return userRepository.findByNicknameContains(Nickname);
  }

  public User getUserById(Long userId) {
    return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다."));
  }

  public void sendFriendRequest(User user, Long userId) {
    User friend = this.getUserById(userId);

    if (friendshipRepository.findByUserAndFriend(user, friend).isPresent()
        || friendshipRepository.findByUserAndFriend(friend, user).isPresent()) {
      //todo friendship - status 에 따라 예외처리
      throw new DataIntegrityViolationException("이미 친구 요청을 보냈습니다.");
    }

    Friendship friendship1 = Friendship.builder()
        .user(user)
        .friend(friend)
        .status(FriendshipStatus.REQUESTED)
        .build();
    Friendship friendship2 = Friendship.builder()
        .user(friend)
        .friend(user)
        .status(FriendshipStatus.REQUESTED)
        .build();

    friendshipRepository.save(friendship1);
    friendshipRepository.save(friendship2);
  }
}
