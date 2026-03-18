package com.transcendence.auth.security;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
public class SecurityConfig {

  private final FortyTwoSuccessHandler fortyTwoSuccessHandler;
  private final FortyTwoCampusOAuth2UserService fortyTwoCampusOAuth2UserService;

  @Value("${app.frontend.base-url:http://localhost:8080}")
  private String frontendBaseUrl;

  @Value("${app.jwt.cookie-name:access_token}")
  private String cookieName;

  public SecurityConfig(FortyTwoSuccessHandler fortyTwoSuccessHandler,
                        FortyTwoCampusOAuth2UserService fortyTwoCampusOAuth2UserService) {
    this.fortyTwoSuccessHandler = fortyTwoSuccessHandler;
    this.fortyTwoCampusOAuth2UserService = fortyTwoCampusOAuth2UserService;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/public/**", "/oauth2/**", "/login/**", "/debug/**", "/me", "/callback").permitAll()
        .anyRequest().authenticated()
      )
      .oauth2Login(oauth -> oauth
        .redirectionEndpoint(ep -> ep.baseUri("/callback"))
        .userInfoEndpoint(u -> u.userService(fortyTwoCampusOAuth2UserService))
        .failureHandler((req, res, ex) -> res.sendRedirect(frontendBaseUrl + "/login?error=oauth_failed"))
        .successHandler(fortyTwoSuccessHandler)
      )
      .logout(logout -> logout
        .logoutUrl("/logout")
        .addLogoutHandler((request, response, authentication) -> {
          Cookie cookie = new Cookie(cookieName, "");
          cookie.setMaxAge(0);
          cookie.setPath("/");
          cookie.setHttpOnly(true);
          cookie.setSecure(true);
          response.addCookie(cookie);
        })
        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
      );

    return http.build();
  }
}
