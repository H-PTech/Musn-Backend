package com.hnptech.musn.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hnptech.musn.entity.enums.StreamingApp;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"provider", "provider_id"})
    }
)
public class User extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String provider;

  @Column(nullable = false)
  private String providerId;

  @Column(nullable = false, unique = true)
  private String nickname;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private StreamingApp streamingApp;

  @JsonProperty("isPush")
  @Column(nullable = false)
  private boolean isPush;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Friendship> friends = new ArrayList<>();

  @Builder
  public User(String provider, String providerId, String nickname, StreamingApp streamingApp, boolean isPush) {
    this.provider = provider;
    this.providerId = providerId;
    this.nickname = nickname;
    this.streamingApp = streamingApp;
    this.isPush = isPush;
  }
}
