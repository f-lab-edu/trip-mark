package com.tripmark.domain.point.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tripmark.domain.point.model.PointHistory;
import com.tripmark.domain.point.repository.PointHistoryRepository;
import com.tripmark.global.common.ResultCase;
import com.tripmark.global.exception.GlobalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PointServiceTest {

  @Mock
  private PointHistoryRepository pointHistoryRepository;

  @InjectMocks
  private PointService pointService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("포인트 적립 성공 테스트")
  void addPoints_success() {
    //given
    Long userId = 1L;
    int amount = 100;
    String source = "Login Reward";

    //when
    pointService.addPoints(userId, amount, source);

    //then
    verify(pointHistoryRepository, times(1)).save(any(PointHistory.class));
  }

  @Test
  @DisplayName("포인트 차감 성공 테스트")
  void deductPoints_Success() {
    //given
    Long userId = 1L;
    int amount = 100;
    when(pointHistoryRepository.findTotalPointsByUserId(userId)).thenReturn(100);
    //when
    pointService.deductPoints(userId, amount, "Purchase");

    //then
    verify(pointHistoryRepository, times(1)).save(any(PointHistory.class));
  }

  @Test
  @DisplayName("포인트 부족으로 차감 실패 테스트")
  void deductPoints_InsufficientPoints() {
    // Given
    Long userId = 1L;
    int amount = 200;
    when(pointHistoryRepository.findTotalPointsByUserId(userId)).thenReturn(100);

    // When & Then
    assertThatThrownBy(() -> pointService.deductPoints(userId, amount, "Purchase"))
            .isInstanceOf(GlobalException.class)
            .hasMessage(ResultCase.POINT_NOT_ENOUGH.getMessage());
  }
}
