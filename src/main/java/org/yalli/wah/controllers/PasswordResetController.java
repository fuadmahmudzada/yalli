package org.yalli.wah.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yalli.wah.dtos.PasswordResetDto;
import org.yalli.wah.payloads.ApiResponse;
import org.yalli.wah.services.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    @Autowired
    private UserService userService;

    // Step 1: Request password reset (send OTP)
    @PostMapping("/reset-password/request")
    public ResponseEntity<ApiResponse> requestPasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        ApiResponse response = userService.requestPasswordReset(email);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    // Step 2: Verify OTP
    @PostMapping("/reset-password/verify")
    public ResponseEntity<ApiResponse> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        ApiResponse response = userService.verifyOtp(email, otp);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    // Step 3: Reset password
    @PostMapping("/reset-password/reset")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody PasswordResetDto passwordResetDto) {
        ApiResponse response = userService.resetPassword(passwordResetDto);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
