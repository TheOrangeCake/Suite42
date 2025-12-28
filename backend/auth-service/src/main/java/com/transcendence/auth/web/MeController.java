package com.transcendence.auth.web;

import java.util.Map;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {

  @GetMapping("/me")
  public Map<String, Object> me(OAuth2AuthenticationToken token) {
    // token.getPrincipal().getAttributes() contains the /v2/me JSON
    return token.getPrincipal().getAttributes();
  }
}
