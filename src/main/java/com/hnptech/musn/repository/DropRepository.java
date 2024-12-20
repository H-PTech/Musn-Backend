package com.hnptech.musn.repository;

import com.hnptech.musn.entity.MusicDrop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DropRepository extends JpaRepository<MusicDrop, Long> {

  List<MusicDrop> findAllByLatBetweenAndLngBetween(float swLat, float neLat, float swLng, float neLng);
}
