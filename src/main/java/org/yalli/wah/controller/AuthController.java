//package org.yalli.wah.controller;
//
//import java.security.Principal;
//import java.util.Map;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class AuthController {
//
//    // A simple endpoint to verify successful login
//    @GetMapping("/api/loginSuccess")
//    public Map<String, Object> loginSuccess(Principal principal) {
//        // The Principal contains the authenticated user information.
//        // Spring Security automatically maps the OAuth2User attributes.
//        return Map.of(
//                "message", "Login successful!",
//                "user", principal.getName()
//        );
//    }
//
//    // Example of a protected endpoint returning OAuth2 user attributes
//    @GetMapping("/api/user")
//    public Object user(Principal principal) {
//        // If you need more attributes than just the name, you can cast principal
//        // to OAuth2AuthenticationToken and get the OAuth2User details.
//
//        return principal;
//    }
//
//    // A public endpoint
//    @GetMapping("/")
//    public String home() {
//        return "Welcome! Visit a protected endpoint (e.g. /api/user) to trigger login.";
//    }
//}
