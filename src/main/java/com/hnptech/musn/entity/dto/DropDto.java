package com.hnptech.musn.entity.dto;


// 좋아요 여부에 따른 데이터를 제공하기 위한 드랍 DTO

import com.hnptech.musn.entity.MusicDrop;
import lombok.Data;

@Data
public class DropDto {
  private MusicDrop musicDrop;
  private boolean like;
}
