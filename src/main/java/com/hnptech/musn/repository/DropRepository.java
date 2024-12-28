package com.hnptech.musn.repository;

import com.hnptech.musn.entity.MusicDrop;
import com.hnptech.musn.entity.dto.MusicAndVideoCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DropRepository extends JpaRepository<MusicDrop, Long> {
  @Query(value = "SELECT * FROM music_drop WHERE " +
          "(6371 * acos(cos(radians(:lat)) * cos(radians(lat)) * cos(radians(lng) - radians(:lng)) + sin(radians(:lat)) * sin(radians(lat)))) <= :radius",
          nativeQuery = true)
  List<MusicDrop> findAllWithinRadius(@Param("lat") float lat,
                                      @Param("lng") float lng,
                                      @Param("radius") float radius);

  @Query(value = "SELECT * FROM music_drop WHERE " +
          "(6371 * acos(cos(radians(:lat)) * cos(radians(lat)) * cos(radians(lng) - radians(:lng)) + sin(radians(:lat)) * sin(radians(lat)))) <= :radius and type = :type",
          nativeQuery = true)
  List<MusicDrop> findAllWithinRadiusByType(@Param("lat") float lat,
                                            @Param("lng") float lng,
                                            @Param("radius") float radius,
                                            @Param("type") int type);


  @Query(value = "SELECT count(*) FROM music_drop WHERE " +
          "(6371 * acos(cos(radians(:lat)) * cos(radians(lat)) * cos(radians(lng) - radians(:lng)) + sin(radians(:lat)) * sin(radians(lat)))) <= :radius",
          nativeQuery = true)
  int countByLatBetweenAndLngBetween(@Param("lat") float lat,
                                     @Param("lng") float lng,
                                     @Param("radius") float radius);


  @Query(value = "SELECT count(*) FROM music_drop WHERE " +
          "(6371 * acos(cos(radians(:lat)) * cos(radians(lat)) * cos(radians(lng) - radians(:lng)) + sin(radians(:lat)) * sin(radians(lat)))) <= :radius and type = :type",
          nativeQuery = true)
  int countByLatBetweenAndLngBetweenAndType(@Param("lat") float lat,
                                            @Param("lng") float lng,
                                            @Param("radius") float radius,
                                            @Param("type") int type);


  void deleteById(long id);

  List<MusicDrop> findByUserId(Long userId);

  @Modifying
  @Query("update MusicDrop p set p.views = p.views + 1 where p.id = :id")
  int updateViews(@Param("id") Long id);


  @Modifying
  @Query("UPDATE MusicDrop m SET m.likeCount = m.likeCount + 1 WHERE m.id = :id")
  int incrementLike(@Param("id") Long id);

  @Modifying
  @Query("UPDATE MusicDrop m SET m.likeCount = m.likeCount - 1 WHERE m.id = :id")
  int decrementLike(@Param("id") Long id);


}
