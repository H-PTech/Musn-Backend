package com.hnptech.musn.repository;

import com.hnptech.musn.entity.Friendship;
import com.hnptech.musn.entity.User;
import com.hnptech.musn.entity.enums.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
  Optional<Friendship> findByUserAndFriend(User user, User friend);

  List<Friendship> findByUserAndStatus(User user, FriendshipStatus status);
}