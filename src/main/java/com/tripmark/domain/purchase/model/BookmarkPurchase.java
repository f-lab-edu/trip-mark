package com.tripmark.domain.purchase.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkPurchase {

  private Long id;
  private Long userId;
  private Long bookmarkId;
  private LocalDateTime purchaseAt;
}
