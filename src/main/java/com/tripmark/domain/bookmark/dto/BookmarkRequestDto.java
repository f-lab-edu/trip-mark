package com.tripmark.domain.bookmark.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookmarkRequestDto(
    @NotBlank(message = "제목은 필수 입력 사항입니다.")
    String title,

    @NotBlank(message = "설명은 필수 입력 사항입니다.")
    String description,

    @NotBlank(message = "URL은 필수 입력 사항입니다.")
    String url,

    @NotNull(message = "대륙 ID는 필수 입력 사항입니다.")
    Long continentId,

    @NotNull
    Long countryId,

    @NotNull(message = "도시 ID는 필수 입력 사항입니다.")
    Long cityId,

    @NotNull(message = "포인트는 필수 입력 사항입니다.")
    Integer pointsRequired

) {

}
