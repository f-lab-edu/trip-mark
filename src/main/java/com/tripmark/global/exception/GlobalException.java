package com.tripmark.global.exception;

import com.tripmark.global.common.ResultCase;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

  private final ResultCase resultCase;

  public GlobalException(ResultCase resultCase) {
    super(resultCase.getMessage());
    this.resultCase = resultCase;
  }
}
