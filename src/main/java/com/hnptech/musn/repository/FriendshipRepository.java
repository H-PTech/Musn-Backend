package com.hnptech.musn.repository;

import com.hnptech.musn.entity.Friendship;
import com.hnptech.musn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
  Optional<Friendship> findByUserAndFriend(User user, User friend);
}