package com.tripmark.domain.bookmark.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateStatusRequestDto(@NotBlank(message = "status는 필수값입니다.") String status) {

}
