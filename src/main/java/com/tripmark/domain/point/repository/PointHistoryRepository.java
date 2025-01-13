package com.tripmark.domain.point.repository;

import com.tripmark.domain.point.model.PointHistory;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PointHistoryRepository {

  void save(PointHistory pointHistory);

  List<PointHistory> findByUserId(Long userId);

  Integer findTotalPointsByUserId(Long userId);

}
