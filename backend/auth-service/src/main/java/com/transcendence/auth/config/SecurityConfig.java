package com.transcendence.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public endpoints (match both with and without context-path)
                .requestMatchers(
                    new AntPathRequestMatcher("/public/**"),
                    new AntPathRequestMatcher("/api/auth/public/**"),
                    new AntPathRequestMatcher("/actuator/**")
                ).permitAll()
                // OAuth endpoints must stay accessible
                .requestMatchers(
                    new AntPathRequestMatcher("/oauth2/**"),
                    new AntPathRequestMatcher("/login/**"),
                    new AntPathRequestMatcher("/api/auth/oauth2/**"),
                    new AntPathRequestMatcher("/api/auth/login/**")
                ).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(Customizer.withDefaults());

        return http.build();
    }
}
