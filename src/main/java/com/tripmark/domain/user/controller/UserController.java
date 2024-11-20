package com.tripmark.domain.user.controller;

import com.tripmark.domain.user.dto.UserRegistrationRequestDto;
import com.tripmark.domain.user.dto.UserRegistrationResponseDto;
import com.tripmark.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<UserRegistrationResponseDto> registerUser(@RequestBody UserRegistrationRequestDto requestDto) {
    UserRegistrationResponseDto responseDto = userService.registerUser(requestDto);
    return ResponseEntity.ok(responseDto);
  }
}
