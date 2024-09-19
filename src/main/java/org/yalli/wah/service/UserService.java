package org.yalli.wah.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.yalli.wah.dao.entity.UserEntity;
import org.yalli.wah.dao.repository.UserRepository;
import org.yalli.wah.mapper.UserMapper;
import org.yalli.wah.model.dto.LoginDto;
import org.yalli.wah.model.dto.PasswordResetDto;
import org.yalli.wah.model.dto.RegisterDto;
import org.yalli.wah.model.exception.InvalidInputException;
import org.yalli.wah.payloads.ApiResponse;
import org.yalli.wah.util.PasswordUtil;
import org.yalli.wah.util.TokenUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final TokenUtil tokenUtil;
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private EmailService emailService;
    public HashMap<String, String> login(LoginDto loginDto) {
        log.info("ActionLog.login.start email {}", loginDto.getEmail());
        UserEntity userEntity = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> {
            log.info("ActionLog.login.error email {} not found", loginDto.getEmail());
            return new InvalidInputException("EMAIL_NOT_FOUND");
        });

        if (!passwordUtil.matches(loginDto.getPassword(), userEntity.getPassword())) {
            throw new InvalidInputException("INVALID_PASSWORD");
        }

        userEntity.setAccessToken(tokenUtil.generateToken());
        userEntity.setTokenExpire(LocalDateTime.now().plusMinutes(30));
        if (!userEntity.isEmailConfirmed()) {
            log.info("ActionLog.login.error email {} not confirmed", loginDto.getEmail());
            throw new InvalidInputException("EMAIL_NOT_CONFIRMED");
        }

        userRepository.save(userEntity);
        log.info("ActionLog.login.end email {}", loginDto.getEmail());
        return new HashMap<>() {{
            put("access-token", userEntity.getAccessToken());
        }};
    }


    public void register(RegisterDto registerDto) {
        log.info("ActionLog.register.start email {}", registerDto.getEmail());
        UserEntity userEntity = UserMapper.INSTANCE.mapRegisterDtoToUser(registerDto);
        userEntity.setPassword(passwordUtil.encode(userEntity.getPassword()));

        //send otp
        String otp = generateOtp();
        userEntity.setOtp(otp);
        userEntity.setOtpExpiration(LocalDateTime.now().plusSeconds(30));
        emailService.sendConfirmationEmail(registerDto.getEmail(), otp);

        userRepository.save(userEntity);
        log.info("ActionLog.register.end email {}", registerDto.getEmail());
    }

    public HashMap<String, String> refreshToken(String accessToken) {
        UserEntity userEntity = userRepository.findByAccessToken(accessToken).orElseThrow(() -> {
                    log.info("ActionLog.refreshToken.error accessToken {} not found", accessToken);
                    return new InvalidInputException("ACCESS_TOKEN_NOT_FOUND");
                }
        );
        userEntity.setAccessToken(tokenUtil.generateToken());
        userEntity.setTokenExpire(LocalDateTime.now().plusMinutes(30));
        userRepository.save(userEntity);
        return new HashMap<>() {{
            put("access-token", userEntity.getAccessToken());
        }};
    }






    public ApiResponse requestPasswordReset(String email) {
        log.info("ActionLog.requestPasswordReset.start email {}", email);
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            String otp = generateOtp();
            user.setOtp(otp);
            user.setOtpExpiration(LocalDateTime.now().plusSeconds(30));
            userRepository.save(user);

            emailService.sendOtp(user.getEmail(), otp);
            log.info("ActionLog.requestPasswordReset.success OTP sent email {}", email);
            return new ApiResponse("OTP sent to email", true);
        }

        log.info("ActionLog.requestPasswordReset.error email not found {}", email);
        return new ApiResponse("Email not found", false);
    }

    public ApiResponse verifyOtp(String email, String otp) {
        log.info("ActionLog.verifyOtp.start email {}", email);
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            if(user.getOtp()!=null && user.getOtp().equals(otp)){
                if(user.getOtpExpiration().isAfter(LocalDateTime.now())){
                    user.setOtpVerified(true);
                    userRepository.save(user);
                    log.info("ActionLog.verifyOtp.success OTP has verified email {}", email);
                    return new ApiResponse("Otp has verified", true);
                }
                else{
                    log.info("ActionLog.verifyOtp.success OTP has expired {}", email);
                    return new ApiResponse("Otp has expired", false);
                }

            }

        }
        return new ApiResponse("Invalid otp", false);
    }
    public ApiResponse resetPassword(PasswordResetDto passwordResetDto) {
        log.info("ActionLog.resetPassword.start email {}", passwordResetDto.getEmail());


        if (!passwordResetDto.getNewPassword().equals(passwordResetDto.getNewPasswordRepeat())) {
            log.info("ActionLog.resetPassword.error passwords do not match email {}", passwordResetDto.getEmail());
            return new ApiResponse("Passwords do not match!", false);
        }


        Optional<UserEntity> userOptional = userRepository.findByEmail(passwordResetDto.getEmail());

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();


            if (!user.isOtpVerified()) {
                log.info("ActionLog.resetPassword.error OTP not verified email {}", passwordResetDto.getEmail());
                return new ApiResponse("OTP has not been verified", false);
            }


            if (bCryptPasswordEncoder.matches(passwordResetDto.getNewPassword(), user.getPassword())) {
                log.info("ActionLog.resetPassword.error new password is same as old one email {}", passwordResetDto.getEmail());
                return new ApiResponse("New password can't be the same  with old ", false);
            }


            String hashedPassword = bCryptPasswordEncoder.encode(passwordResetDto.getNewPassword());
            user.setPassword(hashedPassword);
            user.setOtpVerified(false);

            user.setOtpExpiration(null);
            userRepository.save(user);

            log.info("ActionLog.resetPassword.success email {}", passwordResetDto.getEmail());
            return new ApiResponse("Password reset successful", true);
        } else {
            log.info("ActionLog.resetPassword.error invalid email {}", passwordResetDto.getEmail());
            return new ApiResponse("Invalid email or user not found", false);
        }
    }

    public String generateOtp (){
        return String.valueOf(new Random().nextInt(999999));
    }

    public ApiResponse confirmEmail(String email, String otp){
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();


            if (user.getOtpExpiration().isBefore(LocalDateTime.now())) {
                log.info("ActionLog.confirmEmail.error OTP expired for email {}", email);
                return new ApiResponse("OTP has expired", false);
            }


            if (!user.getOtp().equals(otp)) {
                log.info("ActionLog.confirmEmail.error Invalid OTP for email {}", email);
                return new ApiResponse("Invalid OTP", false);
            }



            user.setEmailConfirmed(true);


            user.setOtpExpiration(null);


            userRepository.save(user);

            log.info("ActionLog.confirmEmail.success Email confirmed for email {}", email);
            return new ApiResponse("Email confirmed successfully", true);
        }

        log.info("ActionLog.confirmEmail.error User not found for email {}", email);
        return new ApiResponse("Invalid email or user not found", false);
    }

}
