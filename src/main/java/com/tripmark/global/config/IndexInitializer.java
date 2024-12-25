package com.tripmark.global.config;

import com.tripmark.domain.bookmark.service.ElasticsearchBookmarkIndexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IndexInitializer implements CommandLineRunner {

  private final ElasticsearchBookmarkIndexService indexService;

  @Override
  public void run(String... args) {
    try {
      log.info("Elasticsearch 인덱스 초기화 작업 시작...");
      indexService.resetIndex();
    } catch (Exception e) {
      log.error("Elasticsearch 인덱스 초기화 중 오류 발생", e);
    }
  }
}
