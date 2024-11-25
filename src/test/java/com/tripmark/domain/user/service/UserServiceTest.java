package com.tripmark.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tripmark.domain.auth.util.JwtUtil;
import com.tripmark.domain.user.dto.UserRegistrationRequestDto;
import com.tripmark.domain.user.dto.UserRegistrationResponseDto;
import com.tripmark.domain.user.model.User;
import com.tripmark.domain.user.repository.UserMapper;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

  @Autowired
  private UserService userService;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  void testRegisterUser_Success() throws Exception {
    //given
    UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto(
        "newuser@example.com", "Password123!"
    );

    //when
    UserRegistrationResponseDto responseDto = userService.registerUser(requestDto);

    //then
    assertNotNull(responseDto);
    assertNotNull(responseDto.accessToken());
    assertNotNull(responseDto.refreshToken());
    assertTrue(jwtUtil.valdateToken(responseDto.accessToken()));
    assertTrue(jwtUtil.valdateToken(responseDto.refreshToken()));
    assertEquals(requestDto.email(), jwtUtil.parseToken(responseDto.accessToken()).getSubject());

    // 데이터베이스에 유저가 저장되었는지 확인
    Optional<User> userOptional = userMapper.findByEmail(requestDto.email());
    assertTrue(userOptional.isPresent());
    User user = userOptional.get();
    assertEquals(requestDto.email(), user.getEmail());

    // 비밀번호가 암호화되었는지 확인
    assertNotEquals(requestDto.password(), user.getPassword());
    assertTrue(passwordEncoder.matches(requestDto.password(), user.getPassword()));
  }

}
