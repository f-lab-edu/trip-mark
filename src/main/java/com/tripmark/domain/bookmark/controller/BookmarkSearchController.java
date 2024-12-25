package com.tripmark.domain.bookmark.controller;

import com.tripmark.domain.bookmark.dto.BookmarkDto;
import com.tripmark.domain.bookmark.dto.SearchResponseDto;
import com.tripmark.domain.bookmark.service.ElasticsearchBookmarkSearchService;
import com.tripmark.global.common.RestResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookmarkSearchController {

  private final ElasticsearchBookmarkSearchService elasticsearchBookmarkSearchService;

  @GetMapping("/api/bookmarks/search")
  public ResponseEntity<RestResponse<SearchResponseDto>> searchBookmarks(
      @RequestParam(name = "q", required = true) String query,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    List<BookmarkDto> results = elasticsearchBookmarkSearchService.searchBookmarks(query, page, size);
    SearchResponseDto responseDto = new SearchResponseDto(results, page, size, results.size());
    return RestResponse.success(responseDto);
  }
}
