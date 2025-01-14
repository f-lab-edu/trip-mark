package com.tripmark.domain.bookmark.service;

import com.tripmark.domain.bookmark.dto.BookmarkRequestDto;
import com.tripmark.domain.bookmark.dto.BookmarkResponseDto;
import com.tripmark.domain.bookmark.model.Bookmark;
import com.tripmark.domain.bookmark.model.BookmarkStatus;
import com.tripmark.domain.bookmark.repository.BookmarkMapper;
import com.tripmark.domain.location.model.City;
import com.tripmark.domain.location.repository.CityMapper;
import com.tripmark.domain.location.repository.ContinentMapper;
import com.tripmark.domain.location.repository.CountryMapper;
import com.tripmark.domain.user.model.User;
import com.tripmark.domain.user.repository.UserMapper;
import com.tripmark.global.common.ResultCase;
import com.tripmark.global.exception.GlobalException;
import com.tripmark.global.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

  private final BookmarkMapper bookmarkMapper;
  private final UserMapper userMapper;
  private final ContinentMapper continentMapper;
  private final CountryMapper countryMapper;
  private final CityMapper cityMapper;
  private final KafkaProducerService kafkaProducerService;

  public BookmarkResponseDto createBookmark(BookmarkRequestDto requestDto, String email) {
    User user = validateUser(email);

    Long cityId = requestDto.cityId();
    City city = cityMapper.findById(cityId).orElseThrow(() -> new GlobalException(ResultCase.INVALID_INPUT));

    Bookmark bookmark = Bookmark.builder()
            .userId(user.getUserId())
            .title(requestDto.title())
            .description(requestDto.description())
            .url(requestDto.url())
            .pointsRequired(requestDto.pointsRequired())
            .cityId(requestDto.cityId())
            .status(BookmarkStatus.PENDING)
            .build();

    bookmarkMapper.insertBookmark(bookmark);

    return BookmarkResponseDto.builder()
            .bookmarkId(bookmark.getBookmarkId())
            .title(bookmark.getTitle())
            .description(bookmark.getDescription())
            .url(bookmark.getUrl())
            .status(bookmark.getStatus().name().toLowerCase())
            .cityName(city.getName())
            .pointsRequired(bookmark.getPointsRequired())
            .createdAt(bookmark.getCreatedAt())
            .build();
  }

  public BookmarkResponseDto getBookmark(Long bookmarkId, String email) {
    User user = validateUser(email);

    Bookmark bookmark = validateBookmark(bookmarkId);

    if (!BookmarkStatus.APPROVED.equals(bookmark.getStatus()) && !bookmark.getUserId().equals(user.getUserId())) {
      throw new GlobalException(ResultCase.BOOKMARK_NOT_FOUND);
    }

    bookmarkMapper.incrementViewCount(bookmarkId);

    City city = cityMapper.findById(bookmark.getCityId())
            .orElseThrow(() -> new GlobalException(ResultCase.INVALID_INPUT));

    return BookmarkResponseDto.builder()
            .bookmarkId(bookmark.getBookmarkId())
            .title(bookmark.getTitle())
            .description(bookmark.getDescription())
            .url(bookmark.getUrl())
            .status(bookmark.getStatus().name().toLowerCase())
            .cityName(city.getName())
            .pointsRequired(bookmark.getPointsRequired())
            .viewCount(bookmark.getViewCount())
            .recommendationCount(bookmark.getRecommendationCount())
            .createdAt(bookmark.getCreatedAt())
            .build();
  }

  public BookmarkResponseDto updateBookmark(Long bookmarkId, BookmarkRequestDto requestDto, String email) {
    User user = validateUser(email);

    Bookmark bookmark = validateBookmark(bookmarkId);

    if (!bookmark.getUserId().equals(user.getUserId())) {
      throw new GlobalException(ResultCase.BOOKMARK_FORBIDDEN);
    }

    City city = cityMapper.findById(requestDto.cityId())
            .orElseThrow(() -> new GlobalException(ResultCase.INVALID_INPUT));

    bookmark.setTitle(requestDto.title());
    bookmark.setDescription(requestDto.description());
    bookmark.setUrl(requestDto.url());
    bookmark.setCityId(requestDto.cityId());
    bookmark.setPointsRequired(requestDto.pointsRequired());
    bookmark.setStatus(BookmarkStatus.PENDING);
    bookmarkMapper.updateBookmark(bookmark);

    return BookmarkResponseDto.builder()
            .bookmarkId(bookmark.getBookmarkId())
            .title(bookmark.getTitle())
            .description(bookmark.getDescription())
            .url(bookmark.getUrl())
            .status(bookmark.getStatus().name().toLowerCase())
            .cityName(city.getName())
            .pointsRequired(bookmark.getPointsRequired())
            .createdAt(bookmark.getCreatedAt())
            .build();
  }

  public void deleteBookmark(Long bookmarkId, String email) {
    User user = validateUser(email);

    Bookmark bookmark = validateBookmark(bookmarkId);

    if (!bookmark.getUserId().equals(user.getUserId())) {
      throw new GlobalException(ResultCase.BOOKMARK_FORBIDDEN);
    }

    bookmarkMapper.deleteBookmark(bookmarkId);
  }

  @Transactional
  public void updateBookmarkStatus(Long bookmarkId, String newStatus) {

    Bookmark bookmark = validateBookmark(bookmarkId);

    bookmarkMapper.updateStatus(bookmarkId, newStatus);

    kafkaProducerService.sendMessage("bookmark-status-update",
            String.format("{\"bookmarkId\": %d, \"status\": \"%s\"}", bookmarkId, newStatus));
  }

  private Bookmark validateBookmark(Long bookmarkId) {
    return bookmarkMapper.findById(bookmarkId).orElseThrow(() -> new GlobalException(ResultCase.BOOKMARK_NOT_FOUND));
  }

  private User validateUser(String email) {
    return userMapper.findByEmail(email).orElseThrow(() -> new GlobalException(ResultCase.USER_NOT_FOUND));
  }
}
