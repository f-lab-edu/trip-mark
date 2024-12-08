package com.tripmark.domain.location.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Country {

  private Long countryId;
  private Long continentId;
  private String name;
}
