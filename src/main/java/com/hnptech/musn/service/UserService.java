package com.hnptech.musn.service;

import com.hnptech.musn.entity.Friendship;
import com.hnptech.musn.entity.User;
import com.hnptech.musn.entity.dto.UserDto;
import com.hnptech.musn.entity.enums.FriendshipStatus;
import com.hnptech.musn.exception.BadRequestException;
import com.hnptech.musn.exception.ForbiddenException;
import com.hnptech.musn.exception.NotFoundException;
import com.hnptech.musn.repository.FriendshipRepository;
import com.hnptech.musn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final FriendshipRepository friendshipRepository;

  public void updateNickName(User user, String nickname) {
    userRepository.findByNickname(nickname)
        .ifPresent(u -> {
          throw new DataIntegrityViolationException("이미 사용중인 닉네임입니다.");
        });

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

    // 본인에게 친구요청 시
    if (Objects.equals(user.getId(), friend.getId())) {
      throw new BadRequestException("본인에게 친구 요청 불가");
    }

    if (friendshipRepository.findByUserAndFriend(user, friend).isPresent()
        || friendshipRepository.findByUserAndFriend(friend, user).isPresent()) {
      //todo friendship - status 에 따라 예외처리
      throw new DataIntegrityViolationException("이미 친구 요청을 보냈습니다.");
    }

    //todo 요청 시에 양쪽에 관계를 만들면 요청 조회 시 친구에게 요청을 보냈지만 나도 조회 및 응답 가능
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

  public List<UserDto> findByUserAndStatus(User user, FriendshipStatus status) {
    List<Friendship> friendships = friendshipRepository.findByUserAndStatus(user, status);
    return friendships.stream()
        .map(friendship -> new UserDto(friendship.getFriend()))
        .collect(Collectors.toList());
  }

  public void updateFriendshipStatus(User user, Long requestId, FriendshipStatus status) {
    Friendship friendship = friendshipRepository.findById(requestId).orElseThrow(() -> new BadRequestException("잘못된 요청입니다."));

    if (friendship.getStatus() != FriendshipStatus.REQUESTED
        || !user.getId().equals(friendship.getUser().getId())) {
      throw new ForbiddenException("권한이 없습니다.");
    }

    friendship.setStatus(status);
    friendshipRepository.save(friendship);
  }
}
