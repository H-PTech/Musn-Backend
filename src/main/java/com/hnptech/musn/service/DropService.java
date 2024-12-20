package com.hnptech.musn.service;

import com.hnptech.musn.entity.MusicDrop;

import com.hnptech.musn.repository.DropRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DropService {

  private final DropRepository dropRepository;

  public List<MusicDrop> getDropList(float neLat, float neLng, float swLat, float swLng) {
    return dropRepository.findAllByLatBetweenAndLngBetween(swLat, neLat, swLng, neLng);
  }
}
