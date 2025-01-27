package com.tripmark.domain.purchase.repository;

import com.tripmark.domain.purchase.model.BookmarkPurchase;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookmarkPurchaseRepository {

  void save(BookmarkPurchase bookmarkPurchase);

  List<BookmarkPurchase> findByUserId(Long userId);
}
