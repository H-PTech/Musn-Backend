package com.hnptech.musn.entity;

import com.hnptech.musn.entity.enums.StreamingApp;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String provider;

  @Column(nullable = false, unique = true)
  private String providerId;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private StreamingApp streamingApp;

  @Column(nullable = false)
  private boolean isPush;

  @Builder
  public User(String provider, String providerId, String nickname, StreamingApp streamingApp, boolean isPush) {
    this.provider = provider;
    this.providerId = providerId;
    this.nickname = nickname;
    this.streamingApp = streamingApp;
    this.isPush = isPush;
  }
}
