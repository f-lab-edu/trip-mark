package com.tripmark.domain.user.controller;

import com.tripmark.domain.user.dto.UserRegistrationRequestDto;
import com.tripmark.domain.user.dto.UserRegistrationResponseDto;
import com.tripmark.domain.user.service.UserService;
import com.tripmark.global.common.RestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<RestResponse<UserRegistrationResponseDto>> registerUser(@Valid @RequestBody UserRegistrationRequestDto requestDto) {
    UserRegistrationResponseDto responseDto = userService.registerUser(requestDto);
    return RestResponse.success(responseDto);
  }
}
