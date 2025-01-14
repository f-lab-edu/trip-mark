package com.tripmark.domain.point.controller;

import com.tripmark.domain.point.dto.PointHistoryDto;
import com.tripmark.domain.point.dto.PointResponseDto;
import com.tripmark.domain.point.service.PointService;
import com.tripmark.global.common.RestResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {

  private final PointService pointService;

  @GetMapping("/balance")
  public ResponseEntity<RestResponse<PointResponseDto>> getPointBalance(@RequestParam Long userId) {
    PointResponseDto pointBalance = pointService.getCurrentPoints(userId);
    return RestResponse.success(pointBalance);
  }

  @GetMapping("/history")
  public ResponseEntity<RestResponse<List<PointHistoryDto>>> getPointHistory(@RequestParam Long userId) {
    List<PointHistoryDto> pointHistory = pointService.getPointHistory(userId);
    return RestResponse.success(pointHistory);
  }
}
