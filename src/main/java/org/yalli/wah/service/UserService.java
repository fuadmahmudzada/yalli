package org.yalli.wah.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yalli.wah.dao.entity.UserEntity;
import org.yalli.wah.dao.repository.UserRepository;
import org.yalli.wah.mapper.UserMapper;
import org.yalli.wah.model.dto.LoginDto;
import org.yalli.wah.model.dto.RegisterDto;
import org.yalli.wah.model.dto.Token;
import org.yalli.wah.model.exception.InvalidInputException;
import org.yalli.wah.util.PasswordUtil;
import org.yalli.wah.util.TokenUtil;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final TokenUtil tokenUtil;

    public HashMap<String, String> login(LoginDto loginDto) {
        log.info("ActionLog.login.start username {}", loginDto.getEmail());
        UserEntity userEntity = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> {
            log.info("ActionLog.login.error email {} not found", loginDto.getEmail());
            return new InvalidInputException("EMAIL_NOT_FOUND");
        });

        if (!passwordUtil.matches(loginDto.getPassword(), userEntity.getPassword())) {
            throw new InvalidInputException("INVALID_PASSWORD");
        }

        userEntity.setAccessToken(new Token(tokenUtil.generateToken(), LocalDateTime.now().plusMinutes(30)));
        userRepository.save(userEntity);
        log.info("ActionLog.login.end username {}", loginDto.getEmail());
        return new HashMap<>() {{
            put("access-token", userEntity.getAccessToken().getToken());
        }};
    }

    public void register(RegisterDto registerDto) {
        log.info("ActionLog.register.start username {}", registerDto.getEmail());
        UserEntity userEntity = UserMapper.INSTANCE.mapRegisterDtoToUser(registerDto);
        userEntity.setPassword(passwordUtil.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
        log.info("ActionLog.register.end username {}", registerDto.getEmail());
    }
}
