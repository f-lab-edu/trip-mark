package com.tripmark.global.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class InvalidInputResponseDto {

  private String field;
  private String message;
}
