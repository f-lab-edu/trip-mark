package com.tripmark.domain.bookmark.repository;

import com.tripmark.domain.bookmark.model.Bookmark;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface BookmarkMapper {

  void insertBookmark(Bookmark bookmark);

  Optional<Bookmark> findById(Long bookmarkId);

  List<Bookmark> findAll();

  void incrementViewCount(Long bookmarkId);

  void updateBookmark(Bookmark bookmark);

  void updateStatus(@Param("bookmarkId") Long bookmarkId, @Param("status") String status);

  void deleteBookmark(Long bookmarkId);
}
