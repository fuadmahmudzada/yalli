//package org.yalli.wah.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//import org.yalli.wah.dao.entity.UserEntity;
//import org.yalli.wah.dao.repository.UserRepository;
//import org.yalli.wah.service.UserService;
//
//import java.util.Optional;
//@Service
//public class CustomOAuth2UserService1 extends DefaultOAuth2UserService {
//
//    private final UserRepository userRepository;
//
//    @Autowired
//    public CustomOAuth2UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oauth2User = super.loadUser(userRequest);
//
//        // Process the user information
//        return processOAuth2User(oauth2User);
//    }
//
//    private OAuth2User processOAuth2User(OAuth2User oauth2User) {
//        String email = oauth2User.getAttribute("email");
//
//        // Find existing user or register a new one
//        UserEntity user = userRepository.findByEmail(email)
//                .orElseGet(() -> registerNewUser(oauth2User));
//
//        // Update existing user information if necessary
//        user.setFullName(oauth2User.getAttribute("name"));
//        userRepository.save(user);
//
//        return oauth2User;
//    }
//
//    private UserEntity registerNewUser(OAuth2User oauth2User) {
//        UserEntity user = new UserEntity();
//        user.setEmail(oauth2User.getAttribute("email"));
//        user.setFullName(oauth2User.getAttribute("name"));
//        user.setEmailConfirmed(true); // Verified by Google
//        return userRepository.save(user);
//    }
//}
