package com.hnptech.musn.repository;

import com.hnptech.musn.entity.DropLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DropLikeRepository extends JpaRepository<DropLike, Long> {
  void deleteByDropIdAndUserId(Long dropId, Long userId);
}
