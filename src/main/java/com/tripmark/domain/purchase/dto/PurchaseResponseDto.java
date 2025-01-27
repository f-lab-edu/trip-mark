package com.tripmark.domain.purchase.dto;

import java.time.LocalDateTime;

public record PurchaseResponseDto(
        Long bookmarkId,
        String title,
        String url,
        Integer pointRequired,
        String status,
        LocalDateTime purchaseAt
) {

}
