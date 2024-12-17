package com.tripmark.domain.bookmark.controller;

import com.tripmark.domain.bookmark.dto.BookmarkRequestDto;
import com.tripmark.domain.bookmark.dto.BookmarkResponseDto;
import com.tripmark.domain.bookmark.service.BookmarkService;
import com.tripmark.global.common.RestResponse;
import com.tripmark.global.common.ResultCase;
import com.tripmark.global.exception.GlobalException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

  private final BookmarkService bookmarkService;

  @PostMapping
  public ResponseEntity<RestResponse<BookmarkResponseDto>> createBookmark(
      @Valid @RequestBody BookmarkRequestDto requestDto,
      @AuthenticationPrincipal OAuth2User principal) {

    if (principal == null) {
      throw new GlobalException(ResultCase.LOGIN_REQUIRED);
    }

    String email = principal.getAttribute("email");
    BookmarkResponseDto responseDto = bookmarkService.createBookmark(requestDto, email);

    return RestResponse.success(responseDto);
  }

  @GetMapping("/{bookmarkId}")
  public ResponseEntity<RestResponse<BookmarkResponseDto>> getBookmark(
      @PathVariable Long bookmarkId,
      @AuthenticationPrincipal OAuth2User principal) {

    String email = principal.getAttribute("email");
    BookmarkResponseDto responseDto = bookmarkService.getBookmark(bookmarkId, email);

    return RestResponse.success(responseDto);
  }

  @PutMapping("/{bookmarkId}")
  public ResponseEntity<RestResponse<BookmarkResponseDto>> updateBookmark(
      @PathVariable Long bookmarkId,
      @Valid @RequestBody BookmarkRequestDto requestDto,
      @AuthenticationPrincipal OAuth2User principal) {

    String email = principal.getAttribute("email");
    BookmarkResponseDto responseDto = bookmarkService.updateBookmark(bookmarkId, requestDto, email);

    return RestResponse.success(responseDto);
  }

  @DeleteMapping("/{bookmarkId}")
  public ResponseEntity<RestResponse<String>> deleteBookmark(
      @PathVariable Long bookmarkId,
      @AuthenticationPrincipal OAuth2User principal) {
    if (principal == null) {
      throw new GlobalException(ResultCase.LOGIN_REQUIRED);
    }

    String email = principal.getAttribute("email");
    bookmarkService.deleteBookmark(bookmarkId, email);

    return RestResponse.success("북마크가 삭제되었습니다.");
  }
}
