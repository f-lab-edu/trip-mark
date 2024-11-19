package com.tripmark.domain.auth.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class JwtUtilTest {

  @Autowired
  private JwtUtil jwtUtil;

  @Test
  void testGenerateAndParseToken() {
    // Given: 이메일 정보
    String email = "testuser@gmail.com";

    // When: JWT 발급
    String token = jwtUtil.generateToken(email);

    // Then: JWT 파싱 및 검증
    Claims claims = jwtUtil.parseToken(token);
    assertEquals(email, claims.getSubject());
    assertTrue(jwtUtil.valdateToken(token));
  }

  @Test
  void testInvalidToken() {
    // Given: 유효하지 않은 토큰
    String invalidToken = "invalid.jwt.token";

    // Then: 토큰 검증 실패
    assertFalse(jwtUtil.valdateToken(invalidToken));
  }
}
