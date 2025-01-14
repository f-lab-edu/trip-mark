package com.tripmark.domain.location.repository;

import com.tripmark.domain.location.model.Continent;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContinentMapper {

  Optional<Continent> findById(Long continentId);
  
  List<Continent> findAll();
}
