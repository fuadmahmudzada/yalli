package org.yalli.wah.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.yalli.wah.model.dto.LoginDto;
import org.yalli.wah.model.dto.PasswordResetDto;
import org.yalli.wah.model.dto.RegisterDto;
import org.yalli.wah.payloads.ApiResponse;
import org.yalli.wah.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public HashMap<String, String> login(@RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegisterDto registerDto) {
        userService.register(registerDto);
    }

    @PatchMapping("/refresh/token")
    public HashMap<String,String> refreshToken(@RequestHeader(value = "access-token") String accessToken) {
        return userService.refreshToken(accessToken);
    }

    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse> confirm(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        ApiResponse response = userService.confirmEmail(email, otp);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/reset-password/request")
    public ResponseEntity<ApiResponse> requestPasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        ApiResponse response = userService.requestPasswordReset(email);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/reset-password/verify")
    public ResponseEntity<ApiResponse> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        ApiResponse response = userService.verifyOtp(email, otp);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/reset-password/reset")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody PasswordResetDto passwordResetDto) {
        ApiResponse response = userService.resetPassword(passwordResetDto);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

}