package com.tripmark.domain.bookmark.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripmark.domain.bookmark.dto.BookmarkRequestDto;
import com.tripmark.domain.bookmark.dto.BookmarkResponseDto;
import com.tripmark.domain.bookmark.model.BookmarkStatus;
import com.tripmark.domain.bookmark.service.BookmarkService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookmarkController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class BookmarkControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BookmarkService bookmarkService;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {
//    // Mock OAuth2User
//    Map<String, Object> attributes = Map.of("email", "user@example.com");
//    OAuth2User principal = new DefaultOAuth2User(Collections.emptyList(), attributes, "email");
//
//    // Mock Authentication
//    OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(principal, null, "google");
//    SecurityContextHolder.getContext().setAuthentication(authentication);
    SecurityContextHolder.clearContext(); // SecurityContext 초기화

  }

  // 1.1 판매자가 새로운 북마크를 성공적으로 업로드한다.
  @Test
  public void testCreateBookmark_Success() throws Exception {
    //given
    BookmarkRequestDto requestDto = new BookmarkRequestDto(
        "북마크 제목",
        "북마크 설명",
        "http://map.google.com/",
        1L,
        1L,
        1L,
        100
    );

    BookmarkResponseDto responseDto = BookmarkResponseDto.builder()
        .bookmarkId(1L)
        .title("북마크 제목")
        .description("북마크 설명")
        .url("http://map.google.com/")
        .status(BookmarkStatus.PENDING.name().toLowerCase())
        .cityName("서울")
        .pointsRequired(100)
        .createdAt(LocalDateTime.now())
        .build();

    when(bookmarkService.createBookmark(any(BookmarkRequestDto.class), any(String.class)))
        .thenReturn(responseDto);

    Map<String, Object> attributes = Map.of("email", "user@example.com");
    OAuth2User principal = new DefaultOAuth2User(
        Collections.emptyList(),
        attributes,
        "email"
    );
    Authentication authentication = new OAuth2AuthenticationToken(principal, null, "google");

    //when & then
    mockMvc.perform(post("/api/bookmarks")
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.bookmarkId").value(1))
        .andExpect(jsonPath("$.data.title").value("북마크 제목"))
        .andExpect(jsonPath("$.data.description").value("북마크 설명"))
        .andExpect(jsonPath("$.data.url").value("http://map.google.com/"))
        .andExpect(jsonPath("$.data.status").value("pending"))
        .andExpect(jsonPath("$.data.cityName").value("서울"))
        .andExpect(jsonPath("$.data.pointsRequired").value(100))
        .andExpect(jsonPath("$.data.createdAt").exists());
  }

  // 1.2: 판매자가 필수 입력 필드를 누락하여 업로드에 실패한다.
  @Test
  public void testCreateBookmark_MissingRequiredFields() throws Exception {
    //given
    BookmarkRequestDto requestDto = new BookmarkRequestDto(
        "", // 제목 누락
        "북마크 설명",
        "", // URL 누락
        null, // 대륙 ID 누락
        1L,
        1L,
        100
    );

    //when & then
    mockMvc.perform(post("/api/bookmarks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.code").value(2001))
        .andExpect(jsonPath("$.message").value("유효하지 않은 입력값입니다."))
        .andExpect(jsonPath("$.data[*].field").value(org.hamcrest.Matchers.containsInAnyOrder("title", "url", "continentId")))
        .andExpect(jsonPath("$.data[*].message").value(org.hamcrest.Matchers.hasItems(
            "제목은 필수 입력 사항입니다.",
            "URL은 필수 입력 사항입니다.",
            "대륙 ID는 필수 입력 사항입니다."
        )));
  }

  // 1.3: 인증되지 않은 사용자가 북마크 업로드를 시도한다.
  @Test
  public void testCreateBookmark_UnauthorizedUser() throws Exception {
    //given
    BookmarkRequestDto requestDto = new BookmarkRequestDto(
        "북마크 제목",
        "북마크 설명",
        "http://map.google.com/",
        1L,
        1L,
        1L,
        100
    );

    //when & then
    mockMvc.perform(post("/api/bookmarks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
        .andExpect(jsonPath("$.code").value(1005)) // ResultCase.LOGIN_REQUIRED의 코드
        .andExpect(jsonPath("$.message").value("로그인이 필요합니다."));
  }

}