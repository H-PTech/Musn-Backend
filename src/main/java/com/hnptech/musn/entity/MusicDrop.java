package com.hnptech.musn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MusicDrop extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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

  @Column(nullable = false)
  private int views;

  @Column(nullable = false)
  private int likeCount;

  @Column(nullable = false)
  private int type;

  // music : type - 1
  @Column(nullable = true)
  private String code;

  @Column(nullable = true)
  private String title;

  // video : type - 2
  @Column(nullable = true)
  private String url;


}
