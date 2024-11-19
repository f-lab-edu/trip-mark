package com.tripmark.domain.auth.service;

import com.tripmark.domain.auth.model.OAuth2UserInfoDto;
import com.tripmark.domain.user.model.User;
import com.tripmark.domain.user.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserMapper userMapper;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) {
    OAuth2User oAuth2User = super.loadUser(userRequest);

    String email = oAuth2User.getAttribute("email");
    String provider = userRequest.getClientRegistration().getRegistrationId();

    OAuth2UserInfoDto userInfoDto = new OAuth2UserInfoDto(email, provider);

    User user = userMapper.findByEmail(email).orElseGet(() -> {
      User newUser = new User();
      newUser.setEmail(userInfoDto.email());
      newUser.setUsername("");
      newUser.setPassword("");
      newUser.setCurrentPoints(0);
      userMapper.insertUser(newUser);
      return newUser;
    });
    return oAuth2User;
  }
}
