package com.tripmark.domain.bookmark.dto;

import java.util.List;

public record SearchResponseDto(
    List<BookmarkDto> results,
    int page,
    int size,
    int totalResults
) {

}
