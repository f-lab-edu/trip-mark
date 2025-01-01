package com.tripmark.domain.bookmark.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripmark.domain.bookmark.dto.BookmarkStatusMessage;
import com.tripmark.domain.bookmark.model.Bookmark;
import com.tripmark.domain.bookmark.repository.BookmarkMapper;
import com.tripmark.global.common.ResultCase;
import com.tripmark.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkKafkaConsumerService {

  private final BookmarkMapper bookmarkMapper;
  private final ElasticsearchBookmarkIndexService elasticsearchBookmarkIndexService;
  private final ObjectMapper objectMapper;

  @KafkaListener(topics = "bookmark-status-update", groupId = "bookmark-status-group")
  public void consume(String message) {
    try {
      BookmarkStatusMessage statusMessage = objectMapper.readValue(message, BookmarkStatusMessage.class);
      log.info("전달받은 Kafka 메시지: {}", statusMessage);

      if ("APPROVED".equalsIgnoreCase(statusMessage.status())) {
        Bookmark bookmark = bookmarkMapper.findById(statusMessage.bookmarkId()).orElseThrow(() -> new GlobalException(ResultCase.BOOKMARK_NOT_FOUND));

        elasticsearchBookmarkIndexService.indexBookmark(bookmark);
        log.info("Elasticsearch에 북마크가 색인되었습니다. bookmarkId: {}", bookmark.getBookmarkId());
      }
    } catch (Exception e) {
      log.error("Kafka 메시지 처리 중 오류가 발생했습니다.", e);
    }
  }
}
