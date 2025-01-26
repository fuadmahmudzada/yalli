//package org.yalli.wah.util;
//package com.sallu.helper;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.sallu.entity.AccountVerification;
//import com.sallu.entity.OauthToken;
//import com.sallu.entity.ResetPasswordToken;
//import org.yalli.wah.dao.entity.UserEntity;
//import com.sallu.model.EmailDetails;
//import  org.yalli.wah.model.dto.LoginResponseDto;
//import com.sallu.repository.AccountVerificationRepository;
//import com.sallu.repository.OauthTokenRepository;
//import com.sallu.repository.ResetPasswordTokenRepository;
//import com.sallu.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.env.Environment;
//import org.springframework.http.*;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.security.SecureRandom;
//import java.time.LocalDateTime;
//import java.util.Base64;
//import java.util.UUID;
//
//@Component
//@Transactional
//public class LoginHelper {
//    @Autowired
//    PasswordEncoder passwordEncoder;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    OauthTokenRepository tokenRepository;
//    @Autowired
//    AccountVerificationRepository accountVerificationRepository;
//    @Autowired
//    ResetPasswordTokenRepository resetPasswordTokenRepository;
//    @Autowired
//    EmailSender emailSender;
//    @Autowired
//    Environment environment;
//
//    @Value("${clientId}")
//    String clientId;
//    @Value("${clientSecret}")
//    String clientSecret;
//
////    public User registerUser(String firstName, String lastName, String email, String password) {
////        User user = new User();
////        user.setEnabled(true);
////        user.setEmailVerified(false);
////        user.setRole("USER");
////        user.setFirstName(firstName);
////        user.setLastName(lastName);
////        user.setEmail(email);
////        user.setPassword(passwordEncoder.encode(password));
////
////        user = userRepository.save(user);
////
////        sendAccountVerificationMail(user);
////        return user;
////    }
//
//    private void sendAccountVerificationMail(UserEntity user) {
//        AccountVerification verification = new AccountVerification();
//        verification.setId(UUID.randomUUID().toString());
//        verification.setUser(user);
//
//        accountVerificationRepository.save(verification);
//
//        String link = "http://" + environment.getProperty("server.address", "localhost") + ":" +
//                environment.getProperty("server.port", "8080") + "/accountverification" +
//                "?id=" + verification.getId();
//
//        EmailDetails emailDetails = new EmailDetails();
//        emailDetails.setRecipient(user.getEmail());
//        emailDetails.setSubject("Account Verification");
//        String body = """
//                      Thank you for signing up.
//                      Please verify your account by clicking the link :
//                      """ + link;
//        emailDetails.setBody(body);
//
//        emailSender.sendSimpleMail(emailDetails);
//    }
//
//    public LoginResponseDto login(String username, String password) {
//        UserEntity user = userRepository.findByEmail(username);
//
//        if(user != null && passwordEncoder.matches(password, user.getPassword())){
//            return saveTokenForUser(user);
//        }
//
//        throw new BadCredentialsException("Invalid username or password");
//    }
//
//    private LoginResponseDto saveTokenForUser(UserEntity user) {
//        LoginResponseDto dto =  generateToken();
//        OauthToken token = new OauthToken();
//        token.setAccessToken(dto.getAccessToken());
//        token.setRefreshToken(dto.getRefreshToken());
//        token.setExpirationTime(dto.getExpirationTime());
//        token.setUser(user);
//
//        tokenRepository.save(token);
//        return dto;
//    }
//
//    private LoginResponseDto generateToken() {
//        LoginResponseDto res = new LoginResponseDto();
//        res.setAccessToken(UUID.randomUUID().toString());
//        res.setRefreshToken(UUID.randomUUID().toString());
//        res.setExpirationTime(LocalDateTime.now().plusHours(1));
//        return res;
//    }
//
//    public LoginResponseDto processGrantCode(String code) {
//        String accessToken = getOauthAccessTokenGoogle(code);
//
//        UserEntity googleUser = getProfileDetailsGoogle(accessToken);
//        UserEntity user = userRepository.findByEmail(googleUser.getEmail());
//
//        if(user == null) {
//            user = registerUser(googleUser.getFirstName(), googleUser.getLastName(), googleUser.getEmail(), googleUser.getPassword());
//        }
//
//        return saveTokenForUser(user);
//
//    }
//
//
//
//    private UserEntity getProfileDetailsGoogle(String accessToken) {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setBearerAuth(accessToken);
//
//        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
//
//        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
//        JsonObject jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class);
//
//        UserEntity user = new UserEntity();
//        user.setEmail(jsonObject.get("email").toString().replace("\"", ""));
//        user.setFirstName(jsonObject.get("name").toString().replace("\"", ""));
//        user.setLastName(jsonObject.get("given_name").toString().replace("\"", ""));
//        user.setPassword(UUID.randomUUID().toString());
//
//        return user;
//    }
//    private String getOauthAccessTokenGoogle(String code) {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("code", code);
//        params.add("redirect_uri", "http://localhost:8080/grantcode");
//        params.add("client_id", clientId);
//        params.add("client_secret", clientSecret);
//        params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile");
//        params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
//        params.add("scope", "openid");
//        params.add("grant_type", "authorization_code");
//
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);
//
//        String url = "https://oauth2.googleapis.com/token";
//        String response = restTemplate.postForObject(url, requestEntity, String.class);
//        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
//
//        return jsonObject.get("access_token").toString().replace("\"", "");
//    }
//
//
//
////    public String logout(String email) {
////        User user = userRepository.findByEmail(email);
////        tokenRepository.deleteByUser(user);
////        return "Signed out successfully!";
////    }
//
//    public LoginResponseDto refreshAccessToken(String refreshToken) {
//        OauthToken oauthToken = tokenRepository.findByRefreshToken(refreshToken);
//        if(oauthToken == null) {
//            throw new BadCredentialsException("Invalid refresh token");
//        }
//        LoginResponseDto res = saveTokenForUser(oauthToken.getUser());
//        tokenRepository.delete(oauthToken);
//        return res;
//    }
//}