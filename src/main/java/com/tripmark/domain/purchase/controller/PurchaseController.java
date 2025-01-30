package com.tripmark.domain.purchase.controller;

import com.tripmark.domain.purchase.dto.PurchaseResponseDto;
import com.tripmark.domain.purchase.service.PurchaseService;
import com.tripmark.global.common.RestResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
public class PurchaseController {

  private final PurchaseService purchaseService;

  @PostMapping("/{bookmarkId}")
  public ResponseEntity<RestResponse<PurchaseResponseDto>> purchaseBookmark(
          @PathVariable Long bookmarkId,
          @RequestParam Long userId
  ) {
    PurchaseResponseDto purchaseResponse = purchaseService.purchaseBookmark(bookmarkId, userId);
    return RestResponse.success(purchaseResponse);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<RestResponse<List<PurchaseResponseDto>>> getPurchases(
          @PathVariable Long userId) {
    List<PurchaseResponseDto> purchases = purchaseService.findPurchaseByUserId(userId);
    return RestResponse.success(purchases);
  }
}
