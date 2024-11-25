package com.tripmark.domain.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripmark.domain.user.dto.UserRegistrationRequestDto;
import com.tripmark.domain.user.dto.UserRegistrationResponseDto;
import com.tripmark.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testRegisterUser_Success() throws Exception {
    // Given
    UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto(
        "newuser@example.com", "Password123!"
    );

    UserRegistrationResponseDto responseDto = new UserRegistrationResponseDto("dummy-access-token", "dummy-refresh-token");
    when(userService.registerUser(any(UserRegistrationRequestDto.class))).thenReturn(responseDto);

    // When & Then
    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value("dummy-access-token"))
        .andExpect(jsonPath("$.refreshToken").value("dummy-refresh-token"));
  }
}
