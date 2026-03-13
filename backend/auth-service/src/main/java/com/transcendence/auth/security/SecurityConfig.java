package com.transcendence.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  private final FortyTwoSuccessHandler fortyTwoSuccessHandler;

  public SecurityConfig(FortyTwoSuccessHandler fortyTwoSuccessHandler) {
    this.fortyTwoSuccessHandler = fortyTwoSuccessHandler;
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
        .successHandler(fortyTwoSuccessHandler)
      )
      .logout(logout -> logout.logoutUrl("/logout"));

    return http.build();
  }
}
