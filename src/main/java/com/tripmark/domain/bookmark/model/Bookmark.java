package com.tripmark.domain.bookmark.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Bookmark {

  private Long bookmarkId;
  private Long userId;
  private Long cityId;
  private String title;
  private String description;
  private String url;
  private Integer pointsRequired;
  private Integer viewCount;
  private Integer recommendationCount;
  private BookmarkStatus status;
  private LocalDateTime createdAt;
}
