package com.tripmark.domain.location.repository;

import com.tripmark.domain.location.model.Country;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CountryMapper {

  Optional<Country> findById(Long countryId);

  List<Country> findByContinentId(Long continentId);
}
