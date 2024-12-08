package com.tripmark.domain.location.repository;

import com.tripmark.domain.location.model.Continent;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ContinentMapper {

  @Select("SELECT continent_id AS continentId, name "
      + "FROM continents "
      + "WHERE continent_id = #{continentId}")
  Optional<Continent> findById(Long continentId);
}
