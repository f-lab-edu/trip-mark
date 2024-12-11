package com.tripmark.domain.bookmark.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;


@Builder
public record BookmarkResponseDto(
    Long bookmarkId,
    String title,
    String description,
    String url,
    String status,
    String cityName,
    Integer pointsRequired,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    Integer viewCount,
    Integer recommendationCount,
    LocalDateTime createdAt
) {

}
