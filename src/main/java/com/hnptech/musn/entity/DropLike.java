package com.hnptech.musn.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DropLike {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long userId;    // 좋아요 누른 사용자
  @Column(nullable = false)
  private Long dropId;  // 드랍 ID
}
