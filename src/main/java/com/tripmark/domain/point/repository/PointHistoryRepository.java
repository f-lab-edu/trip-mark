package com.tripmark.domain.point.repository;

import com.tripmark.domain.point.dto.PointHistoryDto;
import com.tripmark.domain.point.model.PointHistory;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface PointHistoryRepository {

  void save(PointHistory pointHistory);

  List<PointHistoryDto> findHistoryByUserId(@Param("userId") Long userId);

  Integer findTotalPointsByUserId(Long userId);

}
