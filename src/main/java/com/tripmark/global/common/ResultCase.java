package com.tripmark.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResultCase {

  // 성공 응답 코드
  SUCCESS(HttpStatus.OK, 0, "정상 처리되었습니다."),

  // 유저
  OAUTH2_AUTH_FAILED(HttpStatus.UNAUTHORIZED, 1000, "OAuth2 인증에 실패했습니다."),
  DUPLICATED_LOGIN_ID(HttpStatus.CONFLICT, 1001, "중복된 로그인ID를 입력하셨습니다."),
  DUPLICATED_NICKNAME(HttpStatus.CONFLICT, 1002, "중복된 닉네임을 입력하셨습니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, 1003, "유저를 찾을 수 없습니다."),
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 1004, "유효하지 않은 토큰입니다."),
  LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, 1005, "로그인이 필요합니다."),
  EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 1006, "만료된 Access Token"),
  EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, 1007, "만료된 Refresh Token"),

  // 데이터 유효성 및 입력 오류
  INVALID_INPUT(HttpStatus.BAD_REQUEST, 2001, "유효하지 않은 입력값입니다.");

  private final HttpStatus httpStatus;
  private final int code;
  private final String message;
}
