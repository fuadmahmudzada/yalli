package org.yalli.wah.services;

import org.yalli.wah.dtos.RegisterDto;
import org.yalli.wah.dtos.UserDto;

import java.util.List;

public interface UserService {
    UserDto register(RegisterDto register);
    boolean confirmEmail(String email, String token);
    UserDto createUser(RegisterDto registerDto);
    List<UserDto> getAllUsers();
}
