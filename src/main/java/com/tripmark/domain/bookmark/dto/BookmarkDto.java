package com.tripmark.domain.bookmark.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record BookmarkDto(
    Long bookmarkId,
    String title,
    String description,
    String cityName,
    LocalDateTime createdAt
) {

}
