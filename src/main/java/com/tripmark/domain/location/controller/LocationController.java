package com.tripmark.domain.location.controller;

import com.tripmark.domain.location.model.City;
import com.tripmark.domain.location.model.Continent;
import com.tripmark.domain.location.model.Country;
import com.tripmark.domain.location.service.LocationService;
import com.tripmark.global.common.RestResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

  private final LocationService locationService;

  @GetMapping("/continents")
  public ResponseEntity<RestResponse<List<Continent>>> getAllContinents() {
    List<Continent> continents = locationService.getAllContinents();
    return RestResponse.success(continents);
  }

  @GetMapping("/continents/{continentId}/countries")
  public ResponseEntity<RestResponse<List<Country>>> getCountriesByContinentId(@PathVariable Long continentId) {
    List<Country> countries = locationService.getCountriesByContinentId(continentId);
    return RestResponse.success(countries);
  }

  @GetMapping("/countries/{countryId}/cities")
  public ResponseEntity<RestResponse<List<City>>> getCitiesByCountryId(@PathVariable Long countryId) {
    List<City> cities = locationService.getCitiesByCountryId(countryId);
    return RestResponse.success(cities);
  }
}
