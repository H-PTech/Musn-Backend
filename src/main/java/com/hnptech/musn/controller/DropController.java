package com.hnptech.musn.controller;

import com.hnptech.musn.config.CustomUserDetails;
import com.hnptech.musn.config.S3Service;
import com.hnptech.musn.entity.MusicDrop;
import com.hnptech.musn.entity.dto.MusicAndVideoCount;
import com.hnptech.musn.service.DropService;
import com.hnptech.musn.util.ApiResult;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/drop")
@RequiredArgsConstructor
public class DropController {

  private final DropService dropService;
  private final S3Service s3Service;

  @GetMapping("/music")
  public ResponseEntity<ApiResult<List<MusicDrop>>> getDropMusic(@RequestParam(defaultValue = "37.5") Float lat,
                                                                 @RequestParam(defaultValue = "126.95") Float lng,
                                                                 @RequestParam(defaultValue = "1.0") Float radius) {
    List<MusicDrop> result = dropService.getDropListByType(lat, lng, radius, 1);
    return ResponseEntity.ok(ApiResult.<List<MusicDrop>>builder()
            .message("반경 내 음악 리스트 반환, 반경 : " + radius + "km")
            .data(result)
            .build());
  }

  @GetMapping("/video")
  public ResponseEntity<ApiResult<List<MusicDrop>>> getDropVideo(@RequestParam(defaultValue = "37.5") Float lat,
                                                                 @RequestParam(defaultValue = "126.95") Float lng,
                                                                 @RequestParam(defaultValue = "1.0") Float radius) {
    List<MusicDrop> result = dropService.getDropListByType(lat, lng, radius, 2);
    return ResponseEntity.ok(ApiResult.<List<MusicDrop>>builder()
            .message("반경 내 영상 리스트 반환, 반경 : " + radius + "km")
            .data(result)
            .build());
  }

  @GetMapping
  public ResponseEntity<ApiResult<List<MusicDrop>>> getDropList(
          @RequestParam(defaultValue = "37.5665") Float lat,
          @RequestParam(defaultValue = "126.9780") Float lng,
          @RequestParam(defaultValue = "1.0") Float radius) { // 반경 기본값: 1km
    List<MusicDrop> result = dropService.getDropList(lat, lng, radius);
    return ResponseEntity.ok(ApiResult.<List<MusicDrop>>builder()
            .message("반경 내 음악&영상 반환, 반경 : " + radius + "km")
            .data(result)
            .build());
  }


  //지도 범위 내 드랍 조회(거리 순)
  @GetMapping("/sort")
  public ResponseEntity<ApiResult<List<MusicDrop>>> getDropListByDistance(
          @RequestParam(defaultValue = "37.5") Float lat,
          @RequestParam(defaultValue = "126.95") Float lng,
          @RequestParam(defaultValue = "1.0") Float radius) {
    List<MusicDrop> result = dropService.getDropListByDistance(lat, lng, radius);
    return ResponseEntity.ok(ApiResult.<List<MusicDrop>>builder()
            .message("반경 내 음악&영상 반환(거리순), 반경 : " + radius + "km")
            .data(result)
            .build());
  }

  // 지도 범위 내 드랍 총 개수 조회
  @GetMapping("/count/all")
  public ResponseEntity<ApiResult<Integer>> getDropListCount(
          @RequestParam(defaultValue = "37.5") Float lat,
          @RequestParam(defaultValue = "126.95") Float lng,
          @RequestParam(defaultValue = "1.0") Float radius) {
    int result = dropService.getDropCount(lat, lng, radius);
    return ResponseEntity.ok(ApiResult.<Integer>builder()
            .message("지도 범위 내 드랍 총 개수 조회")
            .data(result)
            .build());
  }

  // 지도 범위 내 뮤직&비디오 개수 조회
  @GetMapping("/count")
  public ResponseEntity<ApiResult<MusicAndVideoCount>> getMusicAndVideoCount(
          @RequestParam(defaultValue = "37.5") Float lat,
          @RequestParam(defaultValue = "126.95") Float lng,
          @RequestParam(defaultValue = "1.0") Float radius) {
    MusicAndVideoCount result = dropService.getMusicAndVideoCount(lat, lng, radius);
    return ResponseEntity.ok(ApiResult.<MusicAndVideoCount>builder()
            .message("지도 범위 내 드랍 총 개수 조회")
            .data(result)
            .build());
  }

  // 특정 작성자 드랍 조회
  @GetMapping("/user/{userId}")
  public ResponseEntity<ApiResult<List<MusicDrop>>> getDropListByUserId(@PathVariable(value = "userId") long userId) {
    List<MusicDrop> result = dropService.findByUserId(userId);
    return ResponseEntity.ok(ApiResult.<List<MusicDrop>>builder()
            .message("지도 범위 내 드랍 총 개수 조회")
            .data(result)
            .build());
  }

  // 드랍 등록
  // 파일 업로드를 받는 경우는 타입이 2일 경우
  // 타입이 2일 경우 영상의 썸네일과 영상을 멀티파트파일로 받음.
  @PostMapping()
  public ResponseEntity<ApiResult<Object>> addDrop(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                   @RequestParam("video") MultipartFile file1,
                                                   @RequestParam("thumbnailImage") MultipartFile file2,
                                                   @RequestBody MusicDrop drop) {
    long userId = customUserDetails.getUser().getId();
    drop.setUserId(userId);

    if (drop.getType() == 2 && !file1.isEmpty()) { // 타입과 파일 여부 검사 후 s3 업로드
      try {
        drop.setVideo(s3Service.uploadFile(file1));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    if (drop.getType() == 2 && !file2.isEmpty()) {
      try {
        drop.setThumbnailImage(s3Service.uploadFile(file2));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    dropService.save(drop);
    return ResponseEntity.ok(ApiResult.<Object>builder()
            .message("드랍이 성공적으로 등록되었습니다.")
            .data(null)
            .build());
  }

  // 드랍 내용 수정
  @PatchMapping()
  public ResponseEntity<ApiResult<Object>> updateDrop(@RequestBody MusicDrop drop) {
    dropService.save(drop);
    return ResponseEntity.ok(ApiResult.<Object>builder()
            .message("드랍이 성공적으로 수정되었습니다.")
            .data(null)
            .build());
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
  public ResponseEntity<ApiResult<MusicDrop>>  getDropById(@PathVariable long id) {
    MusicDrop result = (MusicDrop)dropService.findById(id).get();
    return ResponseEntity.ok(ApiResult.<MusicDrop>builder()
            .message("드랍 상세 조회입니다.")
            .data(result)
            .build());
  }

  // TODO : 드랍을 조회할 때 user가 좋아요를 누른 게시글인지에 대한 여부는 구현 되어있지 않음
  // 드랍 좋아요
  @PostMapping("/{dropId}/like")
  public ResponseEntity<ApiResult<Object>> addLike(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable long dropId) {
    // user id 가져오기
    long userId = customUserDetails.getUser().getId();
    dropService.insertLike(userId, dropId);
    return ResponseEntity.ok(ApiResult.<Object>builder()
            .message("success")
            .data(null)
            .build());
  }

  // 드랍 좋아요 취소
  @DeleteMapping("/{dropId}/like")
  public ResponseEntity<ApiResult<Object>> deleteLike(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable long dropId) {
    long userId = customUserDetails.getUser().getId();
    dropService.deleteLike(userId, dropId);
    return ResponseEntity.ok(ApiResult.<Object>builder()
            .message("success")
            .data(null)
            .build());
  }
}
