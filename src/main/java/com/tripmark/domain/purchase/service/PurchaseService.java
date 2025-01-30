package com.tripmark.domain.purchase.service;

import com.tripmark.domain.bookmark.model.Bookmark;
import com.tripmark.domain.bookmark.model.BookmarkStatus;
import com.tripmark.domain.bookmark.service.BookmarkService;
import com.tripmark.domain.point.service.PointService;
import com.tripmark.domain.purchase.dto.PurchaseResponseDto;
import com.tripmark.domain.purchase.model.BookmarkPurchase;
import com.tripmark.domain.purchase.repository.BookmarkPurchaseRepository;
import com.tripmark.global.common.ResultCase;
import com.tripmark.global.exception.GlobalException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseService {

  private final BookmarkService bookmarkService;
  private final PointService pointService;
  private final BookmarkPurchaseRepository purchaseRepository;

  @Transactional
  public PurchaseResponseDto purchaseBookmark(Long bookmarkId, Long userId) {

    Bookmark bookmark = bookmarkService.getBookmarkEntity(bookmarkId);
    if (bookmark.getStatus() != BookmarkStatus.APPROVED) {
      throw new GlobalException(ResultCase.BOOKMARK_NOT_AVAILABLE);
    }

    pointService.deductPoints(userId, bookmark.getPointsRequired(), "BOOKMARK_PURCHASE");
    pointService.addPoints(bookmark.getUserId(), bookmark.getPointsRequired(), "BOOKMARK_SALE");

    BookmarkPurchase purchase = BookmarkPurchase.builder()
            .userId(userId)
            .bookmarkId(bookmarkId)
            .purchaseAt(LocalDateTime.now())
            .build();
    purchaseRepository.save(purchase);

    return new PurchaseResponseDto(
            bookmark.getBookmarkId(),
            bookmark.getTitle(),
            bookmark.getUrl(),
            bookmark.getPointsRequired(),
            bookmark.getStatus().name(),
            purchase.getPurchaseAt()
    );
  }

  @Transactional(readOnly = true)
  public List<PurchaseResponseDto> findPurchaseByUserId(Long userId) {
    List<BookmarkPurchase> purchases = purchaseRepository.findByUserId(userId);

    if (purchases.isEmpty()) {
      throw new GlobalException(ResultCase.BOOKMARK_NOT_FOUND);
    }
    return purchases.stream().map(purchase -> {
      Bookmark bookmark = bookmarkService.getBookmarkEntity(purchase.getBookmarkId());
      return new PurchaseResponseDto(
              bookmark.getBookmarkId(),
              bookmark.getTitle(),
              bookmark.getUrl(),
              bookmark.getPointsRequired(),
              bookmark.getStatus().name(),
              purchase.getPurchaseAt()
      );
    }).collect(Collectors.toList());
  }
}
