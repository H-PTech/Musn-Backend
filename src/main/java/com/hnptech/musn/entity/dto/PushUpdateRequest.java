package com.hnptech.musn.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PushUpdateRequest {
  @JsonProperty("isPush")
  private boolean isPush;
}
