package com.hnptech.musn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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
    private String streamingApp;

    @Column(nullable = false)
    private boolean isPush;
}
