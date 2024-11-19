package com.tripmark.domain.auth.model;

public record OAuth2UserInfoDto(
    String email,
    String provider
) {

}
