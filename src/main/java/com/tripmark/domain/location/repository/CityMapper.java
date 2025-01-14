package com.tripmark.domain.location.repository;

import com.tripmark.domain.location.model.City;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CityMapper {

  Optional<City> findById(Long cityId);

  List<City> findByCountryId(Long countryId);
}
