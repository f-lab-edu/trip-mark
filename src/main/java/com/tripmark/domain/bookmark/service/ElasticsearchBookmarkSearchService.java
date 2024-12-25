package com.tripmark.domain.bookmark.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.tripmark.domain.bookmark.dto.BookmarkDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ElasticsearchBookmarkSearchService {

  private final ElasticsearchClient elasticsearchClient;

  public List<BookmarkDto> searchBookmarks(String query, int page, int size) {
    try {
      log.info("북마크 검색: query={}, page={}, size={}", query, page, size);

      SearchRequest searchRequest = SearchRequest.of(s -> s
          .index("bookmarks")
          .query(q -> q
              .multiMatch(m -> m
                  .fields("title", "description", "city_name")
                  .query(query)
              )
          )
          .from(page * size)
          .size(size)
      );

      SearchResponse<BookmarkDto> response = elasticsearchClient.search(searchRequest, BookmarkDto.class);

      List<BookmarkDto> results = new ArrayList<>();
      for (Hit<BookmarkDto> hit : response.hits().hits()) {
        results.add(hit.source());
      }
      log.info("검색 결과 '{}' 반환 {} 결과", query, results.size());
      return results;
    } catch (Exception e) {
      log.error("Elasticsearch 검색 실패: {}", e.getMessage());
      throw new RuntimeException("검색에 실패했습니다.");
    }
  }
}
