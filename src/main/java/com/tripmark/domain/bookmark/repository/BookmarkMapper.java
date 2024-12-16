package com.tripmark.domain.bookmark.repository;

import com.tripmark.domain.bookmark.model.Bookmark;
import java.util.Optional;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

  @Select("""
         SELECT bookmark_id AS bookmarkId,
                user_id AS userId,
                city_id AS cityId,
                title,
                description,
                url,
                points_required AS pointsRequired,
                view_count AS viewCount,
                recommendation_count AS recommendationCount,
                status,
                created_at AS createdAt
         FROM bookmarks
         WHERE bookmark_id = #{bookmarkId}
      """)
  Optional<Bookmark> findById(Long bookmarkId);

  @Update("""
      UPDATE bookmarks
      SET view_count = view_count + 1
      WHERE bookmark_id = #{bookmarkId}
      """)
  void incrementViewCount(Long bookmarkId);

  @Update("""
      UPDATE bookmarks
      SET title = #{title},
          description = #{description},
          url = #{url},
          city_id = #{cityId},
          points_required = #{pointsRequired},
          status = #{status}
      WHERE bookmark_id = #{bookmarkId}
      """)
  void updateBookmark(Bookmark bookmark);
}
