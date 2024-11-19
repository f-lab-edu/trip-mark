package com.tripmark.domain.user.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tripmark.domain.user.model.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserMapperTest {

  @Autowired
  private UserMapper userMapper;

  @Test
  void testSaveAndFindUser() {
    // Given: 새 사용자 데이터
    User newUser = new User();
    newUser.setEmail("testuser@gmail.com");
    newUser.setUsername("Test User");
    newUser.setPassword("");
    newUser.setCurrentPoints(100);

    // When: 사용자 데이터 저장
    userMapper.insertUser(newUser);

    // Then: 사용자 데이터 조회
    Optional<User> savedUser = userMapper.findByEmail("testuser@gmail.com");
    assertTrue(savedUser.isPresent());
    assertEquals("Test User", savedUser.get().getUsername());
  }
}
