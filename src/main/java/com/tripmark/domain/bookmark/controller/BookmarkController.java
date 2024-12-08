package com.tripmark.domain.bookmark.controller;

import com.tripmark.domain.bookmark.dto.BookmarkRequestDto;
import com.tripmark.domain.bookmark.dto.BookmarkResponseDto;
import com.tripmark.domain.bookmark.service.BookmarkService;
import com.tripmark.global.common.RestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
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

    String email = principal.getAttribute("email");
    BookmarkResponseDto responseDto = bookmarkService.createBookmark(requestDto, email);
    
    return RestResponse.success(responseDto);
  }
}
