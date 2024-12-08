package com.tripmark.domain.location.repository;

import com.tripmark.domain.location.model.City;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CityMapper {

  @Select("SELECT city_id AS cityId, country_id AS countryId, name "
      + "FROM cities "
      + "WHERE city_id = #{cityId}")
  Optional<City> findById(Long cityId);
}
