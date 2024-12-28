package com.hnptech.musn.controller;

import com.hnptech.musn.config.CustomUserDetails;
import com.hnptech.musn.entity.MusicDrop;
import com.hnptech.musn.entity.dto.MusicAndVideoCount;
import com.hnptech.musn.service.DropService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/drop")
@RequiredArgsConstructor
public class DropController {

  private final DropService dropService;

  @GetMapping("/test")
  public void test() {
    MusicDrop drop = new MusicDrop();
    drop.setCode("ss");
    float a = 123;
    drop.setLng(a);
    drop.setLat(a);
    drop.setContent("ss");
    drop.setUserId(1L);
    drop.setViews(0);
    drop.setLikeCount(0);
    drop.setType(1);

    dropService.save(drop);
  }

  @GetMapping("test1")
  public ResponseEntity<?> test1() {
    return ResponseEntity.ok(dropService.findAll());
  }

  @GetMapping
  public ResponseEntity<?> getDropList(
          @RequestParam(defaultValue = "37.5") Float neLat,
          @RequestParam(defaultValue = "126.95") Float neLng,
          @RequestParam(defaultValue = "37.6") Float swLat,
          @RequestParam(defaultValue = "127") Float swLng) {
    System.out.println("swLat: " + swLat);
    System.out.println("neLat: " + neLat);
    System.out.println("swLng: " + swLng);
    System.out.println("neLng: " + neLng);
    List<MusicDrop> drops = dropService.getDropList(swLat, neLat, swLng, neLng);
    return ResponseEntity.ok(drops);
  }


  //지도 범위 내 드랍 조회(거리 순)
  @GetMapping("/{userLat}/{userLng}")
  public ResponseEntity<?> getDropListByDistance(
          @RequestParam(defaultValue = "37.5") float neLat,
          @RequestParam(defaultValue = "126.95") float neLng,
          @RequestParam(defaultValue = "37.6") float swLat,
          @RequestParam(defaultValue = "127") float swLng,
          // 현재 위치
          @PathVariable(value = "userLat") float userLat,
          @PathVariable(value = "userLng") float userLng) {
    List<MusicDrop> drops = dropService.getDropListByDistance(swLat, neLat, swLng, neLng, userLat, userLng);
    return ResponseEntity.ok(drops);
  }

  // 지도 범위 내 드랍 총 개수 조회
  @GetMapping("/count/all")
  public ResponseEntity<?> getDropListCount(@RequestParam(defaultValue = "37.5") float neLat,
                                            @RequestParam(defaultValue = "126.95") float neLng,
                                            @RequestParam(defaultValue = "37.6") float swLat,
                                            @RequestParam(defaultValue = "127") float swLng) {
    int count = dropService.getDropCount(swLat, neLat, swLng, neLng);
    return ResponseEntity.ok(count);
  }

  // 지도 범위 내 뮤직&비디오 개수 조회
  @GetMapping("/count")
  public ResponseEntity<?> getMusicAndVideoCount(@RequestParam(defaultValue = "37.5") float neLat,
                                                 @RequestParam(defaultValue = "126.95") float neLng,
                                                 @RequestParam(defaultValue = "37.6") float swLat,
                                                 @RequestParam(defaultValue = "127") float swLng) {
    MusicAndVideoCount result = dropService.getMusicAndVideoCount(swLat, neLat, swLng, neLng);
    return ResponseEntity.ok(result);
  }

  // 특정 작성자 드랍 조회
  @GetMapping("/user/{userId}")
  public ResponseEntity<?> getDropListByUserId(@PathVariable(value = "userId") long userId) {
    List<MusicDrop> result = dropService.findByUserId(userId);
    return ResponseEntity.ok(null);
  }

  // 드랍 등록
  @PostMapping()
  public ResponseEntity<?> addDrop(@RequestBody MusicDrop drop) {
    dropService.save(drop);
    return ResponseEntity.ok("success");
  }

  // 드랍 내용 수정
  @PatchMapping()
  public ResponseEntity<?> updateDrop(@RequestBody MusicDrop drop) {
    dropService.save(drop);
    return ResponseEntity.ok("success");
  }

  // 드랍 삭제
//  @DeleteMapping("/{id}")
//  public ResponseEntity<?> deleteDrop(@PathVariable long id) {
//    // 권한 검사?
//    dropService.deleteById(id);
//    return ResponseEntity.ok("success");
//  }

  // 드랍 상세 조회
  @GetMapping("/{id}")
  public ResponseEntity<?> getDropById(@PathVariable long id) {
    Optional<MusicDrop> drop = dropService.findById(id);
    return ResponseEntity.ok(drop.get());
  }

  // 드랍 좋아요
  @PostMapping("/{dropId}/like")
  public ResponseEntity<?> addLike(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable long dropId) {
    // user id 가져오기
    long userId = customUserDetails.getUser().getId();
    dropService.insertLike(userId, dropId);
    return ResponseEntity.ok("success");
  }

  // 드랍 좋아요 취소
  @DeleteMapping("/{dropId}/like")
  public ResponseEntity<?> deleteLike(@PathVariable long dropId) {
    long userId = 1;
    dropService.deleteLike(userId, dropId);
    return ResponseEntity.ok("success");
  }


}
