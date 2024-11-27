package com.tripmark.global.common;

import org.springframework.validation.FieldError;

public final class InvalidInputMapper {

  public static InvalidInputResponseDto toInvalidInputResponseDto(FieldError fieldError) {
    return InvalidInputResponseDto.builder()
        .field(fieldError.getField())
        .message(fieldError.getDefaultMessage())
        .build();
  }
}
