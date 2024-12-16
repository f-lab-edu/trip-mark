package com.tripmark.domain.bookmark.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripmark.domain.bookmark.dto.BookmarkRequestDto;
import com.tripmark.domain.bookmark.dto.BookmarkResponseDto;
import com.tripmark.domain.bookmark.model.BookmarkStatus;
import com.tripmark.domain.bookmark.service.BookmarkService;
import com.tripmark.global.common.ResultCase;
import com.tripmark.global.exception.GlobalException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    // Mock OAuth2User
    Map<String, Object> attributes = Map.of("email", "user@example.com");
    OAuth2User principal = new DefaultOAuth2User(Collections.emptyList(), attributes, "email");

    // Mock Authentication
    OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(principal, null, "google");
    SecurityContextHolder.getContext().setAuthentication(authentication);
//    SecurityContextHolder.clearContext(); // SecurityContext 초기화

  }

  @Test
  @DisplayName("1.1: 새로운 북마크를 성공적으로 업로드한다.")
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

  @Test
  @DisplayName("1.2: 필수 입력 필드를 누락하여 북마크 업로드에 실패한다.")
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

  @Test
  @DisplayName("1.3: 인증되지 않은 사용자가 북마크 업로드를 시도한다.")
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

  @Test
  @DisplayName("2.1: 북마크를 성공적으로 조회한다.")
  public void testGetBookMark_Success() throws Exception {
    //given
    BookmarkResponseDto responseDto = BookmarkResponseDto.builder()
        .bookmarkId(1L)
        .title("북마크 제목")
        .description("설명")
        .url("http://map.google")
        .status(BookmarkStatus.PENDING.name().toLowerCase())
        .cityName("서울")
        .pointsRequired(100)
        .createdAt(LocalDateTime.now())
        .viewCount(15)
        .recommendationCount(5)
        .build();

    when(bookmarkService.getBookmark(anyLong(), any(String.class)))
        .thenReturn(responseDto);

    Map<String, Object> attributes = Map.of("email", "user@example.com");
    OAuth2User principal = new DefaultOAuth2User(Collections.emptyList(), attributes, "email");
    Authentication authentication = new OAuth2AuthenticationToken(principal, null, "google");

    //when & then
    mockMvc.perform(get("/api/bookmarks/1")
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.bookmarkId").value(1))
        .andExpect(jsonPath("$.data.title").value("북마크 제목"))
        .andExpect(jsonPath("$.data.description").value("설명"))
        .andExpect(jsonPath("$.data.url").value("http://map.google"))
        .andExpect(jsonPath("$.data.status").value("pending"))
        .andExpect(jsonPath("$.data.cityName").value("서울"))
        .andExpect(jsonPath("$.data.pointsRequired").value(100))
        .andExpect(jsonPath("$.data.viewCount").value(15))
        .andExpect(jsonPath("$.data.recommendationCount").value(5))
        .andExpect(jsonPath("$.data.createdAt").exists());
  }

  @Test
  @DisplayName("2.2: 북마크가 존재하지 않을 경우 GlobalException이 발생한다.")
  public void testGetBookmark_NotFound() throws Exception {
    //given
    Long nonExistentBookmarkId = 999L;

    // 북마크가 없을 경우 GlobalException이 발생하도록 설정
    when(bookmarkService.getBookmark(eq(nonExistentBookmarkId), any(String.class)))
        .thenThrow(new GlobalException(ResultCase.BOOKMARK_NOT_FOUND));

    Map<String, Object> attributes = Map.of("email", "user@example.com");
    OAuth2User principal = new DefaultOAuth2User(Collections.emptyList(), attributes, "email");
    Authentication authentication = new OAuth2AuthenticationToken(principal, null, "google");

    //when & then
    mockMvc.perform(get("/api/bookmarks/" + nonExistentBookmarkId)
            .with(authentication(authentication))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value("NOT_FOUND"))
        .andExpect(jsonPath("$.code").value(3001))
        .andExpect(jsonPath("$.message").value("북마크를 찾을 수 없습니다."))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  @DisplayName("3.1: 북마크를 성공적으로 업데이트한다.")
  public void testUpdateBookmark_Success() throws Exception {
    //given
    BookmarkRequestDto requestDto = new BookmarkRequestDto(
        "수정된 제목",
        "수정된 설명",
        "http://map.google.com/updated",
        1L,
        1L,
        1L,
        150
    );

    BookmarkResponseDto responseDto = BookmarkResponseDto.builder()
        .bookmarkId(1L)
        .title("수정된 제목")
        .description("수정된 설명")
        .url("http://map.google.com/updated")
        .status(BookmarkStatus.APPROVED.name().toLowerCase())
        .cityName("서울")
        .pointsRequired(150)
        .createdAt(LocalDateTime.now())
        .build();

    when(bookmarkService.updateBookmark(any(Long.class), any(BookmarkRequestDto.class), any(String.class)))
        .thenReturn(responseDto);
    //when & then
    mockMvc.perform(put("/api/bookmarks/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.bookmarkId").value(1))
        .andExpect(jsonPath("$.data.title").value("수정된 제목"))
        .andExpect(jsonPath("$.data.description").value("수정된 설명"))
        .andExpect(jsonPath("$.data.url").value("http://map.google.com/updated"))
        .andExpect(jsonPath("$.data.status").value("approved"))
        .andExpect(jsonPath("$.data.cityName").value("서울"))
        .andExpect(jsonPath("$.data.pointsRequired").value(150))
        .andExpect(jsonPath("$.data.createdAt").exists());
  }

  @Test
  @DisplayName("3.2: 북마크 소유자가 아닌 사용자가 북마크를 업데이트하려고 시도한다.")
  public void testUpdateBookmark_Forbidden() throws Exception {
    //given
    BookmarkRequestDto requestDto = new BookmarkRequestDto(
        "수정된 제목",
        "수정된 설명",
        "http://map.google.com/updated",
        1L,
        1L,
        1L,
        150
    );

    // 북마크 소유자가 아닌 경우 예외를 발생시킴
    when(bookmarkService.updateBookmark(any(Long.class), any(BookmarkRequestDto.class), any(String.class)))
        .thenThrow(new GlobalException(ResultCase.BOOKMARK_FORBIDDEN));

    //when & then
    mockMvc.perform(put("/api/bookmarks/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.status").value("FORBIDDEN"))
        .andExpect(jsonPath("$.code").value(3002))
        .andExpect(jsonPath("$.message").value("북마크에 접근할 권한이 없습니다."));
  }

  @Test
  @DisplayName("3.3: 업데이트할 북마크가 존재하지 않을 경우 GlobalException이 발생한다.")
  public void testUpdateBookmark_NotFound() throws Exception {
    //given
    BookmarkRequestDto requestDto = new BookmarkRequestDto(
        "수정된 제목",
        "수정된 설명",
        "http://map.google.com/updated",
        1L,
        1L,
        1L,
        150
    );

    when(bookmarkService.updateBookmark(any(Long.class), any(BookmarkRequestDto.class), any(String.class)))
        .thenThrow(new GlobalException(ResultCase.BOOKMARK_NOT_FOUND));

    //when & then
    mockMvc.perform(put("/api/bookmarks/999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value("NOT_FOUND"))
        .andExpect(jsonPath("$.code").value(3001)) // ResultCase.BOOKMARK_NOT_FOUND 코드
        .andExpect(jsonPath("$.message").value("북마크를 찾을 수 없습니다."));
  }

  @Test
  @DisplayName("3.4: 필수 입력 필드를 누락하여 북마크 업데이트에 실패한다.")
  public void testUpdateBookmark_MissingRequiredFields() throws Exception {
    //given
    BookmarkRequestDto requestDto = new BookmarkRequestDto(
        "", // 제목 누락
        "수정된 설명",
        "", // URL 누락
        null, // 대륙 ID 누락
        1L,
        1L,
        150
    );

    //when & then
    mockMvc.perform(put("/api/bookmarks/1")
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
}