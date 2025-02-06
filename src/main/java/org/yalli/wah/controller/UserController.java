package org.yalli.wah.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import org.yalli.wah.model.dto.*;
import org.yalli.wah.model.enums.Country;
import org.yalli.wah.service.UserService;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

    @RestController
    @RequestMapping("/v1/users")
    @CrossOrigin
    @RequiredArgsConstructor
    @Slf4j
    public class UserController {
        private final UserService userService;


        @PostMapping("/login")
        public ResponseEntity<LoginResponseDto> login() throws AuthenticationException {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() ||
                    "anonymousUser".equals(authentication.getPrincipal())) {
                throw new AuthenticationException("User not authenticated");
            }
            if (authentication == null || !authentication.isAuthenticated() ||
                    "anonymousUser".equals(authentication.getPrincipal())) {
                throw new AuthenticationException("User not authenticated");
            }

            if(authentication instanceof OAuth2AuthenticationToken){
                return userService.googleLogin(authentication);
            }
            else {
                return userService.login(authentication);
            }
        }

        @GetMapping("/googleLogin")
        public Object  google(Principal principal) throws AuthenticationException {

            return principal;
        }

    @GetMapping("/countries")
    @Operation(summary = "get all countries")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getCountries() {
        return Arrays.stream(Country.values())
                .map(Country::getCountryName)
                .collect(Collectors.toList());
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "signup and sending mail for otp verification")
    public void register(@RequestBody RegisterDto registerDto) {
        userService.register(registerDto);
    }

    @PatchMapping("/refresh/token")
    @Operation(summary = "refresh access token")
    public HashMap<String, String> refreshToken(@RequestHeader(value = "access-token") String accessToken) {
        return userService.refreshToken(accessToken);
    }

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "confirming mail with otp verification")
    public void confirm(@RequestBody ConfirmDto confirmDto) {
        userService.confirmEmail(confirmDto);
    }

    @PostMapping("/reset-password/request")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "send otp for password reset")
    public void requestPasswordReset(@RequestBody RequestResetDto requestResetDto) throws Exception {
        userService.requestPasswordReset(requestResetDto);
    }

    @PostMapping("/reset-password/verify")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "verify otp for password reset")
    public void verifyOtp(@RequestBody ConfirmDto confirmDto) {
        userService.verifyOtp(confirmDto);
    }


    @PostMapping("/reset-password")
    @Operation(summary = "change user password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@RequestBody PasswordResetDto passwordResetDto) {
        userService.resetPassword(passwordResetDto);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Page<MemberDto> searchUsers(
            @RequestParam(name = "fullName", required = false) String fullName,
            @RequestParam(name = "country", required = false) String country,
            Pageable pageable
    ) {
        return userService.searchUsers(fullName, country, pageable);
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MemberInfoDto getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestBody MemberUpdateDto memberUpdateDto, @PathVariable Long id) {
        userService.updateUser(memberUpdateDto, id);
    }

    @GetMapping("/send-otp")
    public void sendOtp(String email) {
        userService.sendOtp(email);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete user")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/register/resend-otp")
    @Operation(summary = "send otp for registering again")
    @ResponseStatus(HttpStatus.OK)
    public void resendOtp(@RequestBody SendOtpDto sendOtpDto) {
        userService.resendRegisterOtp(sendOtpDto.getEmail());
    }

}
