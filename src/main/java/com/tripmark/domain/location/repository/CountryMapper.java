package com.tripmark.domain.location.repository;

import com.tripmark.domain.location.model.Country;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CountryMapper {

  @Select("SELECT country_id AS countryId, continent_id AS continentId, name "
      + "FROM countries "
      + "WHERE country_id = #{countryId}")
  Optional<Country> findById(Long countryId);
}
