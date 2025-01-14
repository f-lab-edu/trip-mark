package com.tripmark.domain.point.service;

import com.tripmark.domain.point.model.PointHistory;
import com.tripmark.domain.point.model.PointType;
import com.tripmark.domain.point.repository.PointHistoryRepository;
import com.tripmark.global.common.ResultCase;
import com.tripmark.global.exception.GlobalException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PointService {

  private final PointHistoryRepository pointHistoryRepository;

  @Transactional
  public void addPoints(Long userId, int amount, String source) {
    PointHistory pointHistory = PointHistory.builder()
            .userId(userId)
            .amount(amount)
            .type(PointType.EARN)
            .source(source)
            .createdAt(LocalDateTime.now())
            .build();

    pointHistoryRepository.save(pointHistory);

    log.info("포인트 적립: userId={}, amount={}, source={}", userId, amount, source);
  }

  @Transactional
  public void deductPoints(Long userId, int amount, String source) {

    int currentPoints = pointHistoryRepository.findTotalPointsByUserId(userId);
    if (currentPoints < amount) {
      log.warn("포인트 부족: userId={}, currentPoints={}, requestedAmount={}", userId, currentPoints, amount);
      throw new GlobalException(ResultCase.POINT_NOT_ENOUGH);
    }

    PointHistory pointHistory = PointHistory.builder()
            .userId(userId)
            .amount(-amount)
            .type(PointType.SPEND)
            .source(source)
            .createdAt(LocalDateTime.now())
            .build();

    pointHistoryRepository.save(pointHistory);

    log.info("포인트 차감: userId={}, amount={}, source={}", userId, amount, source);
  }
}
