package com.tripmark.domain.location.service;

import com.tripmark.domain.location.model.City;
import com.tripmark.domain.location.model.Continent;
import com.tripmark.domain.location.model.Country;
import com.tripmark.domain.location.repository.CityMapper;
import com.tripmark.domain.location.repository.ContinentMapper;
import com.tripmark.domain.location.repository.CountryMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

  private final ContinentMapper continentMapper;
  private final CountryMapper countryMapper;
  private final CityMapper cityMapper;

  /**
   * 모든 대륙 정보를 조회해 반환한다.
   *
   * @return List<Continent>
   */
  public List<Continent> getAllContinents() {
    return continentMapper.findAll();
  }

  /**
   * 특정 대륙에 해당하는 국가 목록을 조회해 반환한다.
   *
   * @param continentId
   *     대륙 ID
   * @return List<Country>
   */
  public List<Country> getCountriesByContinentId(Long continentId) {
    return countryMapper.findByContinentId(continentId);
  }

  /**
   * 특정 국가에 해당하는 도시 목록을 조회해 반환한다.
   *
   * @param countryId
   *     국가 ID
   * @return List<City>
   */
  public List<City> getCitiesByCountryId(Long countryId) {
    return cityMapper.findByCountryId(countryId);
  }
}
