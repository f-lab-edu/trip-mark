package com.tripmark.domain.point.model;

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
public class PointHistory {

  private Long pointId;
  private Long userId;
  private Integer amount;
  private PointType type;
  private String source;
  private LocalDateTime createdAt;
}
