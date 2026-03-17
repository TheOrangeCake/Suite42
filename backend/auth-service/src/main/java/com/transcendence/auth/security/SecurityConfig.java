package com.transcendence.auth.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  private final FortyTwoSuccessHandler fortyTwoSuccessHandler;
  private final FortyTwoCampusOAuth2UserService fortyTwoCampusOAuth2UserService;

  @Value("${app.frontend.base-url:http://localhost:8080}")
  private String frontendBaseUrl;

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
      .logout(logout -> logout.logoutUrl("/logout"));

    return http.build();
  }
}
