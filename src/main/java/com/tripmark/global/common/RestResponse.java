package com.tripmark.global.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class RestResponse<T> {

  private HttpStatus status;
  private Integer code;
  private String message;
  private T data;

  public static <T> ResponseEntity<RestResponse<T>> success(T data) {
    RestResponse<T> response = RestResponse.<T>builder()
        .status(ResultCase.SUCCESS.getHttpStatus())
        .code(ResultCase.SUCCESS.getCode())
        .message(ResultCase.SUCCESS.getMessage())
        .data(data)
        .build();
    return ResponseEntity.status(ResultCase.SUCCESS.getHttpStatus()).body(response);
  }

  public static <T> ResponseEntity<RestResponse<T>> error(ResultCase resultCase, T data) {
    RestResponse<T> response = RestResponse.<T>builder()
        .status(resultCase.getHttpStatus())
        .code(resultCase.getCode())
        .message(resultCase.getMessage())
        .data(data)
        .build();

    return ResponseEntity.status(resultCase.getHttpStatus()).body(response);
  }
}
