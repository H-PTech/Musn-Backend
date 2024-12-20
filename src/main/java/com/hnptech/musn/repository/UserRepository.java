package com.hnptech.musn.repository;

import com.hnptech.musn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByProviderId(String providerId);
    boolean existsByProviderIdAndProvider(String providerId, String provider);
}
