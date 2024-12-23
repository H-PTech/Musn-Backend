package com.hnptech.musn.repository;

import com.hnptech.musn.entity.User;
import com.hnptech.musn.entity.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByIdAndDeleted(long id, boolean deleted);

  Optional<User> findByProviderAndProviderId(String provider, String providerId);

  Optional<User> findByNickname(String nickname);

  boolean existsByProviderIdAndProvider(String providerId, String provider);

  List<UserDto> findByNicknameContains(String nickname);
}
