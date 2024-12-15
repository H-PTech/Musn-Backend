package com.hnptech.musn.entity;

import jakarta.persistence.*;

@Entity
public class MusicDrop extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = true)
  private String category;

  @Column(nullable = false)
  private float lat;

  @Column(nullable = false)
  private float lng;

}
