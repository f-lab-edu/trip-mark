package com.tripmark.domain.auth.handler;

import com.tripmark.domain.auth.util.JwtUtil;
import com.tripmark.domain.user.repository.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

  private final JwtUtil jwtUtil;
  private final UserMapper userMapper;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) throws IOException, SecurityException {

    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    String email = oAuth2User.getAttribute("email");

    String accessToken = jwtUtil.generateToken(email);
    String refreshToken = jwtUtil.generatedRefreshToken(email);

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write("{\"accessToken\": \"" + accessToken + "\", \"refreshToken\": \"" + refreshToken + "\"}");
  }
}
