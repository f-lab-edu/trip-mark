package com.tripmark.global.exception;

import com.tripmark.global.common.ErrorResponseDto;
import com.tripmark.global.common.InvalidInputMapper;
import com.tripmark.global.common.InvalidInputResponseDto;
import com.tripmark.global.common.RestResponse;
import com.tripmark.global.common.ResultCase;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<RestResponse<List<InvalidInputResponseDto>>> handleValidationException(MethodArgumentNotValidException ex) {
    List<InvalidInputResponseDto> invalidInputList = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(InvalidInputMapper::toInvalidInputResponseDto)
        .toList();

    return RestResponse.error(ResultCase.INVALID_INPUT, invalidInputList);
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<RestResponse<List<InvalidInputResponseDto>>> handleBindException(BindException ex) {
    List<InvalidInputResponseDto> invalidInputList = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(InvalidInputMapper::toInvalidInputResponseDto)
        .toList();

    return RestResponse.error(ResultCase.INVALID_INPUT, invalidInputList);
  }

  @ExceptionHandler(GlobalException.class)
  public ResponseEntity<RestResponse<ErrorResponseDto>> handleGlobalException(GlobalException ex) {
    return RestResponse.error(ex.getResultCase(), new ErrorResponseDto());
  }
}
