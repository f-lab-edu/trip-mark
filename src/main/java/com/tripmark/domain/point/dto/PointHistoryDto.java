package com.tripmark.domain.point.dto;

import com.tripmark.domain.point.model.PointType;
import java.time.LocalDateTime;

public record PointHistoryDto(
        Integer amount,
        PointType type,
        String source,
        LocalDateTime createdAt
) {

}
