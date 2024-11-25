package com.tripmark.domain.user.service;

import com.tripmark.domain.auth.util.JwtUtil;
import com.tripmark.domain.user.dto.UserRegistrationRequestDto;
import com.tripmark.domain.user.dto.UserRegistrationResponseDto;
import com.tripmark.domain.user.model.User;
import com.tripmark.domain.user.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserMapper userMapper;
  private final JwtUtil jwtUtil;
  private final PasswordEncoder passwordEncoder;

  public UserRegistrationResponseDto registerUser(UserRegistrationRequestDto requestDto) {
    String email = requestDto.email();
    String password = requestDto.password();

    if (userMapper.findByEmail(email).isPresent()) {
      throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
    }

    String encodedPassword = passwordEncoder.encode(password);

    User newUser = new User();
    newUser.setEmail(email);
    newUser.setUsername("");
    newUser.setPassword(encodedPassword);
    newUser.setCurrentPoints(0);

    userMapper.insertUser(newUser);

    String accessToken = jwtUtil.generateToken(email);
    String refreshToken = jwtUtil.generatedRefreshToken(email);

    return new UserRegistrationResponseDto(accessToken, refreshToken);
  }
}
