package com.hnptech.musn.entity;

import com.hnptech.musn.entity.enums.FriendshipStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "friend_id"})
    }
)
public class Friendship {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "friend_id")
  private User friend;

  @Enumerated(EnumType.STRING)
  private FriendshipStatus status;
}
