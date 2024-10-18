package org.yalli.wah.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.yalli.wah.dao.entity.UserEntity;
import org.yalli.wah.model.dto.*;
import org.yalli.wah.model.dto.ConfirmDto;
import org.yalli.wah.model.dto.LoginDto;
import org.yalli.wah.model.dto.PasswordResetDto;
import org.yalli.wah.model.dto.RegisterDto;
import org.yalli.wah.service.UserService;

import java.util.HashMap;

@RestController
@RequestMapping("/v1/users")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "login")
    public HashMap<String, String> login(@RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
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
    public void requestPasswordReset(@RequestBody RequestResetDto requestResetDto) {
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
    public MemberInfoDto getUser(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestBody MemberInfoDto memberInfoDto){
         userService.updateUser(memberInfoDto);
    }
  
    @GetMapping("/send-otp")
    public void sendOtp(String email) {
        userService.sendOtp(email);
    }
}
