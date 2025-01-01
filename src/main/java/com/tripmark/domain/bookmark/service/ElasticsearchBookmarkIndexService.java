package com.tripmark.domain.bookmark.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.tripmark.domain.bookmark.dto.BookmarkDto;
import com.tripmark.domain.bookmark.model.Bookmark;
import com.tripmark.domain.bookmark.model.BookmarkStatus;
import com.tripmark.domain.bookmark.repository.BookmarkMapper;
import com.tripmark.domain.location.model.City;
import com.tripmark.domain.location.repository.CityMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchBookmarkIndexService {

  private final ElasticsearchClient elasticsearchClient;
  private final BookmarkMapper bookmarkMapper;
  private final CityMapper cityMapper;

  public void resetIndex() throws Exception {
    // 기존 인덱스 삭제
    boolean exists = elasticsearchClient.indices().exists(e -> e.index("bookmarks")).value();
    if (exists) {
      var deleteResponse = elasticsearchClient.indices().delete(d -> d.index("bookmarks"));
      if (deleteResponse.acknowledged()) {
        log.info("기존 인덱스 'bookmarks'가 성공적으로 삭제되었습니다.");
      } else {
        log.error("기존 인덱스 'bookmarks' 삭제에 실패했습니다.");
        return;
      }
    } else {
      log.info("인덱스 'bookmarks'가 존재하지 않습니다. 삭제를 건너뜁니다.");
    }

    // 새로운 인덱스 생성
    var request = co.elastic.clients.elasticsearch.indices.CreateIndexRequest.of(b -> b.index("bookmarks").mappings(
        m -> m.properties("bookmark_id", p -> p.long_(l -> l)).properties("title", p -> p.text(t -> t)).properties("description", p -> p.text(t -> t))
            .properties("city_name", p -> p.text(t -> t)).properties("created_at", p -> p.date(d -> d))));

    var response = elasticsearchClient.indices().create(request);

    if (response.acknowledged()) {
      log.info("인덱스 'bookmarks'가 성공적으로 생성되었습니다.");
    } else {
      log.error("인덱스 'bookmarks' 생성에 실패했습니다.");
      return;
    }

    addDatabaseDataToIndex();
  }

  private void addDatabaseDataToIndex() throws Exception {
    // APPPROVED 상태의 북마크 데이터만 조회
    List<Bookmark> approvedBookmarks = bookmarkMapper.findAll().stream().filter(bookmark -> BookmarkStatus.APPROVED.equals(bookmark.getStatus())).toList();

    if (approvedBookmarks.isEmpty()) {
      log.info("APPROVED 상태의 북마크 데이터가 없습니다.");
      return;
    }

    for (Bookmark bookmark : approvedBookmarks) {
      Optional<City> cityOptional = cityMapper.findById(bookmark.getCityId());
      String cityName = cityOptional.map(City::getName).orElse("Unknown City");

      BookmarkDto dto = BookmarkDto.builder().bookmarkId(bookmark.getBookmarkId()).title(bookmark.getTitle()).description(bookmark.getDescription())
          .cityName(cityName).createdAt(bookmark.getCreatedAt()).build();

      try {
        elasticsearchClient.index(i -> i.index("bookmarks").id(bookmark.getBookmarkId().toString()).document(dto));
        log.info("북마크 ID '{}'가 Elasticsearch에 적재되었습니다.", bookmark.getBookmarkId());
      } catch (Exception e) {
        log.error("북마크 ID '{}' 적재 실패: {}", bookmark.getBookmarkId(), e.getMessage());
      }
    }

    log.info("APPROVED 상태의 북마크 데이터가 모두 Elasticsearch에 적재되었습니다.");
  }

  public void indexBookmark(Bookmark bookmark) {
    try {
      // City 이름 조회
      String cityName = cityMapper.findById(bookmark.getCityId()).map(City::getName).orElse("Unknown City");

      BookmarkDto dto = BookmarkDto.builder().bookmarkId(bookmark.getBookmarkId()).title(bookmark.getTitle()).description(bookmark.getDescription())
          .cityName(cityName).createdAt(bookmark.getCreatedAt()).build();

      // Elasticsearch에 인덱싱
      elasticsearchClient.index(i -> i.index("bookmarks").id(bookmark.getBookmarkId().toString()).document(dto));

      log.info("성공 인덱싱 북마크: {}", bookmark.getBookmarkId());
    } catch (Exception e) {
      log.error("실패 인덱싱 북마크: {}", bookmark.getBookmarkId(), e);
    }
  }
}