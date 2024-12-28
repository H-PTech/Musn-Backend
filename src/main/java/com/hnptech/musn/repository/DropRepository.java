package com.hnptech.musn.repository;

import com.hnptech.musn.entity.MusicDrop;
import com.hnptech.musn.entity.dto.MusicAndVideoCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DropRepository extends JpaRepository<MusicDrop, Long> {
  List<MusicDrop> findAllByLatBetweenAndLngBetween(float swLat, float neLat, float swLng, float neLng);

  int countByLatBetweenAndLngBetween(float swLat, float neLat, float swLng, float neLng);
  int countByLatBetweenAndLngBetweenAndType(float swLat, float neLat, float swLng, float neLng,int type);
  void deleteById(long id);
  List<MusicDrop> findByUserId(Long userId);
  @Modifying
  @Query("update MusicDrop p set p.views = p.views + 1 where p.id = :id")
  int updateViews(@Param("id")Long id);


  @Modifying
  @Query("UPDATE MusicDrop m SET m.likeCount = m.likeCount + 1 WHERE m.id = :id")
  int incrementLike(@Param("id") Long id);

  @Modifying
  @Query("UPDATE MusicDrop m SET m.likeCount = m.likeCount - 1 WHERE m.id = :id")
  int decrementLike(@Param("id") Long id);


}
