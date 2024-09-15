package org.yalli.wah.services;

import org.yalli.wah.dtos.RegisterDto;
import org.yalli.wah.dtos.UserDto;

public interface UserService {
    UserDto register(RegisterDto register);
    boolean confirmEmail(String email, String token);
}
