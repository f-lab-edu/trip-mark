package com.tripmark.domain.bookmark.repository;

import com.tripmark.domain.bookmark.model.Bookmark;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface BookmarkMapper {

  @Insert("""
      INSERT INTO bookmarks (
        user_id,
        city_id,
        title,
        description,
        url,
        points_required,
        status,
        created_at
      ) VALUES (
        #{userId},
        #{cityId},
        #{title},
        #{description},
        #{url},
        #{pointsRequired},
        #{status},
        NOW()
      )
      """)
  @Options(useGeneratedKeys = true, keyProperty = "bookmarkId")
  void insertBookmark(Bookmark bookmark);
}
