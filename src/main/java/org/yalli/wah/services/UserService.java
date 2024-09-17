package org.yalli.wah.services;

import org.yalli.wah.dtos.PasswordResetDto;
import org.yalli.wah.dtos.RegisterDto;
import org.yalli.wah.dtos.UserDto;
import org.yalli.wah.payloads.ApiResponse;

import java.util.List;

public interface UserService {
    ApiResponse requestPasswordReset(String email);
    ApiResponse verifyOtp(String email, String otp);
    ApiResponse resetPassword(PasswordResetDto passwordResetDto);

    UserDto register(RegisterDto register);
    boolean confirmEmail(String email, String token);
    UserDto createUser(RegisterDto registerDto);
    List<UserDto> getAllUsers();
}
