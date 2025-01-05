package com.hnptech.musn.util;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
 * 응답 객체
 * */
@Getter
@NoArgsConstructor
@ToString
public class ApiResult<T> {
  // 임시로 200 지정
  private final int status = 200;
  private T data;
  private String message;
  private Exception exception;


  @Builder
  public ApiResult(T data, String message, Exception exception) {
    this.data = data;
    this.message = message;
    this.exception = exception;
  }
}
