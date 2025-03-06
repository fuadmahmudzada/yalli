package org.yalli.wah.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class Controller {

    @GetMapping("/loginSuccess")
    public Map<String, Object> loginSuccess(@AuthenticationPrincipal OAuth2User principal) {
        return Map.of(
                "name", principal.getAttribute("name"),
                "email", principal.getAttribute("email"),
                "sub", principal.getAttribute("sub")
        );
    }

    @GetMapping("/loginFailure")
    public Map<String, String> loginFailure() {
        return Map.of("error", "Authentication failed");
    }

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return principal.getAttributes();
    }
    @GetMapping("auth/google/callback")
    public ResponseEntity<?> googleAuthSuccess(@AuthenticationPrincipal OAuth2User user) {
        String email = user.getAttribute("email");
        return ResponseEntity.ok(email);
    }
}