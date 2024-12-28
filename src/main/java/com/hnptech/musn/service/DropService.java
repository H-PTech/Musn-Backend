package com.hnptech.musn.service;

import com.hnptech.musn.entity.DropLike;
import com.hnptech.musn.entity.MusicDrop;

import com.hnptech.musn.entity.dto.MusicAndVideoCount;
import com.hnptech.musn.repository.DropLikeRepository;
import com.hnptech.musn.repository.DropRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DropService {

  private final DropRepository dropRepository;
  private final DropLikeRepository dropLikeRepository;

  public List<MusicDrop> findAll(){
    return dropRepository.findAll();
  };
  public List<MusicDrop> findByUserId(Long userId){return dropRepository.findByUserId(userId);};
  public List<MusicDrop> getDropList(float lat, float lng, float radius) {
    return dropRepository.findAllWithinRadius(lat, lng, radius);
  }
  public List<MusicDrop> getDropListByType(float lat, float lng, float radius, int type){
    return dropRepository.findAllWithinRadiusByType(lat, lng, radius, type);
  }

  public List<MusicDrop> getDropListByDistance(float lat, float lng, float radius){
    List<MusicDrop> list = getDropList(lat, lng, radius);
    // 맨하튼 거리 기준 정렬
    list.sort(Comparator.comparingDouble(p -> manhattanDistance(lat, lng, p.getLat(), p.getLng())));
    return list;
  }

  // 맨하튼 거리 구하기
  public static float manhattanDistance(float userLat, float userLng, float targetLat, float targetLng) {
    return Math.abs(userLat - targetLat) + Math.abs(userLng - targetLng);
  }

  public int getDropCount(float lat, float lng, float radius) {
    return dropRepository.countByLatBetweenAndLngBetween(lat, lng, radius);
  }

  public MusicAndVideoCount getMusicAndVideoCount(float lat, float lng, float radius) {
    MusicAndVideoCount temp = new MusicAndVideoCount();
    int mCount = dropRepository.countByLatBetweenAndLngBetweenAndType(lat, lng, radius, 1);
    int vCount = dropRepository.countByLatBetweenAndLngBetweenAndType(lat, lng, radius, 2);
    temp.setMusicCount(mCount);
    temp.setVideoCount(vCount);
    return temp;
  }

  public void save(MusicDrop drop) {
    drop.setViews(0);
    drop.setLikeCount(0);
    MusicDrop resultDrop = dropRepository.save(drop);
  }

  @Transactional
  public Optional<MusicDrop> findById(Long dropId) {
    Optional<MusicDrop> tempDrop = dropRepository.findById(dropId);
    Long id = tempDrop.get().getId();
    System.out.println(id);
    dropRepository.updateViews(id);
    return tempDrop;
  }

  public void delete(MusicDrop drop) {
    dropRepository.delete(drop);
  }

  public void deleteById(long id) {
    dropRepository.deleteById(id);
  }


  @Transactional
  public void insertLike(long userId, long dropId){
    DropLike dropLike = new DropLike();
    dropLike.setUserId(userId);
    dropLike.setDropId(dropId);

    dropLikeRepository.save(dropLike);
    dropRepository.incrementLike(dropId);
  }

  @Transactional
  public void deleteLike(long userId, long dropId){
    dropLikeRepository.deleteByDropIdAndUserId(dropId, userId);
    dropRepository.decrementLike(dropId);
  }
}
