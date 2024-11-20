package com.tripmark.domain.user.dto;

public record UserRegistrationResponseDto(
    String accessToken,
    String refreshToken
) {

}
