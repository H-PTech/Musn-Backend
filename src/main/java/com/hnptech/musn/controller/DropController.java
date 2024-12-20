package com.hnptech.musn.controller;

import com.hnptech.musn.entity.MusicDrop;
import com.hnptech.musn.service.DropService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/drop")
@RequiredArgsConstructor
public class DropController {

  private final DropService dropService;


  @GetMapping()
  public ResponseEntity<?> getDropList(@RequestParam(defaultValue = "37.5") float neLat,
                                       @RequestParam(defaultValue = "126.95") float neLng,
                                       @RequestParam(defaultValue = "37.6") float swLat,
                                       @RequestParam(defaultValue = "127") float swLng) {
    List<MusicDrop> drops = dropService.getDropList(swLat, neLat, swLng, neLng);
    return ResponseEntity.ok(drops);
  }

//  @PostMapping()
//  public ResponseEntity<?> addDrop(@RequestBody MusicDrop drop) {
//
//  }
}
