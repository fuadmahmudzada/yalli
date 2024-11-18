package org.yalli.wah.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.yalli.wah.dao.entity.UserEntity;
import org.yalli.wah.dao.repository.UserRepository;
import org.yalli.wah.mapper.ProfileMapper;
import org.yalli.wah.mapper.UserMapper;
import org.yalli.wah.model.dto.ConfirmDto;
import org.yalli.wah.model.dto.LoginDto;
import org.yalli.wah.model.dto.MemberDto;
import org.yalli.wah.model.dto.MemberInfoDto;
import org.yalli.wah.model.dto.MemberUpdateDto;
import org.yalli.wah.model.dto.PasswordResetDto;
import org.yalli.wah.model.dto.RegisterDto;
import org.yalli.wah.model.dto.RequestResetDto;
import org.yalli.wah.model.exception.InvalidInputException;
import org.yalli.wah.model.exception.InvalidOtpException;
import org.yalli.wah.model.exception.PermissionException;
import org.yalli.wah.model.exception.ResourceNotFoundException;
import org.yalli.wah.util.PasswordUtil;
import org.yalli.wah.util.TokenUtil;
import org.yalli.wah.util.UserSpecification;

import java.text.MessageFormat;
import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.Random;

import java.util.*;

import static org.yalli.wah.model.enums.EmailTemplate.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final TokenUtil tokenUtil;

    private final EmailService emailService;

    public HashMap<String, String> login(LoginDto loginDto) {
        log.info("ActionLog.login.start email {}", loginDto.getEmail());
        UserEntity userEntity = getUserByEmail(loginDto.getEmail());

        if (!passwordUtil.matches(loginDto.getPassword(), userEntity.getPassword())) {
            throw new InvalidInputException("INVALID_PASSWORD");
        }

        userEntity.setAccessToken(tokenUtil.generateToken());
        userEntity.setTokenExpire(LocalDateTime.now().plusMinutes(30));
        if (!userEntity.isEmailConfirmed()) {
            log.info("ActionLog.login.error email {} not confirmed", loginDto.getEmail());
            throw new InvalidInputException("EMAIL_NOT_CONFIRMED");
        }
        userEntity.setNotCompletedFields(0);
        MemberUpdateDto memberUpdateDto = ProfileMapper.INSTANCE.toMemberUpdateDto(userEntity);
        String[] fields = memberUpdateDto.toString().split(",");
        for (String field : fields) {
            if (field.contains("null")) {
                userEntity.setNotCompletedFields(userEntity.getNotCompletedFields() + 1);
            }
        }
        System.out.println(Arrays.toString(fields));

        userRepository.save(userEntity);
        log.info("ActionLog.login.end email {}", loginDto.getEmail());
        return new HashMap<>() {{
            put("accessToken", userEntity.getAccessToken());
            put("fullName", userEntity.getFullName());
            put("country", userEntity.getCountry());
            put("image", userEntity.getProfilePictureUrl());
            put("id", String.valueOf(userEntity.getId()));
        }};
    }

    public void sendOtp(String email) {
        log.info("ActionLog.sendOtp.start email {}", email);
        var userEntity = getUserByEmail(email);
        var otp = generateOtp();
        userEntity.setOtp(otp);
        emailService.sendMail(email, RESET_PASSWORD.getSubject(), formatMessage(RESET_PASSWORD.getBody(), otp));
        userRepository.save(userEntity);
        log.info("ActionLog.sendOtp.end email {}", email);
    }

    public void register(RegisterDto registerDto) {
        log.info("ActionLog.register.start email {}", registerDto.getEmail());
        userRepository.findByEmail(registerDto.getEmail()).ifPresent((user) -> {
            if (user.isEmailConfirmed()) {
                throw new InvalidInputException("EMAIL_ALREADY_EXISTS");
            }
        });
        UserEntity userEntity = userRepository.findByEmail(registerDto.getEmail()).orElse(new UserEntity());
        userEntity = UserMapper.INSTANCE.mapRegisterDtoToUser(registerDto, userEntity);
        userEntity.setPassword(passwordUtil.encode(userEntity.getPassword()));


        //send otp
        String otp = generateOtp();
        userEntity.setOtp(otp);
        userEntity.setOtpExpiration(LocalDateTime.now().plusSeconds(60));
        emailService.sendMail(registerDto.getEmail(), CONFIRMATION.getSubject(),
                formatMessage(CONFIRMATION.getBody(), otp));

        userRepository.save(userEntity);
        log.info("ActionLog.register.end email {}", registerDto.getEmail());
    }

    public HashMap<String, String> refreshToken(String accessToken) {
        UserEntity userEntity = userRepository.findByAccessToken(accessToken).orElseThrow(() -> {
                    log.info("ActionLog.refreshToken.error accessToken {} not found", accessToken);
                    return new ResourceNotFoundException("ACCESS_TOKEN_NOT_FOUND");
                }
        );
        userEntity.setAccessToken(tokenUtil.generateToken());
        userEntity.setTokenExpire(LocalDateTime.now().plusMinutes(30));
        userRepository.save(userEntity);
        return new HashMap<>() {{
            put("accessToken", userEntity.getAccessToken());
        }};
    }


    public void requestPasswordReset(RequestResetDto requestResetDto) {
        log.info("ActionLog.requestPasswordReset.start email {}", requestResetDto.getEmail());
        UserEntity user = userRepository.findByEmail(requestResetDto.getEmail()).orElseThrow(
                () -> {
                    log.info("ActionLog.requestPasswordReset.error email {} not found", requestResetDto.getEmail());
                    return new ResourceNotFoundException("EMAIL_NOT_FOUND");
                }
        );


        user.setOtpExpiration(null);
        user.setOtpVerified(false);

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiration(LocalDateTime.now().plusMinutes(1));
        userRepository.save(user);

        emailService.sendMail(user.getEmail(), RESET_PASSWORD.getSubject(), formatMessage(RESET_PASSWORD.getBody(), otp));
        log.info("ActionLog.requestPasswordReset.success OTP sent email {}", requestResetDto.getEmail());
    }

    public void verifyOtp(ConfirmDto confirmDto) {
        log.info("ActionLog.verifyOtp.start email {}", confirmDto.getEmail());
        UserEntity user = getUserByEmail(confirmDto.getEmail());

        if (user.getOtp() != null && user.getOtp().equals(confirmDto.getOtp())) {
            if (user.getOtpExpiration().isAfter(LocalDateTime.now())) {
                user.setOtpVerified(true);
                userRepository.save(user);
                log.info("ActionLog.verifyOtp.success OTP has verified email {}", confirmDto.getEmail());
            } else {
                log.warn("ActionLog.verifyOtp.warn OTP has expired {}", confirmDto.getEmail());
                throw new InvalidOtpException("OTP_EXPIRED");
            }

        } else {
            log.warn("ActionLog.verifyOtp.warn OTP {} doesn't match with email's {} otp", confirmDto.getOtp(),
                    confirmDto.getEmail());
            throw new InvalidOtpException("INVALID_PASSWORD");
        }
    }

    public void resetPassword(PasswordResetDto passwordResetDto) {
        log.info("ActionLog.resetPassword.start email {}", passwordResetDto.getEmail());

        var user = getUserByEmail(passwordResetDto.getEmail());


        if (!user.isOtpVerified()) {
            log.warn("ActionLog.resetPassword.warn OTP not verified email {}", passwordResetDto.getEmail());
            throw new PermissionException("OTP_NOT_VERIFIED");
        }

        if (passwordUtil.matches(passwordResetDto.getNewPassword(), user.getPassword())) {
            log.info("ActionLog.resetPassword.error new password is same as old one email {}", passwordResetDto.getEmail());
            throw new InvalidInputException("SAME_WITH_OLD_PASSWORD");
        }

        user.setPassword(passwordUtil.encode(passwordResetDto.getNewPassword()));
        user.setOtpVerified(false);

        user.setOtpExpiration(null);
        userRepository.save(user);

        log.info("ActionLog.resetPassword.success email {}", passwordResetDto.getEmail());
    }

    private String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    public void confirmEmail(ConfirmDto confirmDto) {
        UserEntity user = getUserByEmail(confirmDto.getEmail());
        if (user.getOtpExpiration().isBefore(LocalDateTime.now())) {
            log.info("ActionLog.confirmEmail.error OTP expired for email {}", confirmDto.getEmail());
            throw new InvalidOtpException("OTP_EXPIRED");
        }
        if (!user.getOtp().equals(confirmDto.getOtp())) {
            log.info("ActionLog.confirmEmail.error Invalid OTP for email {}", confirmDto.getEmail());
            throw new InvalidOtpException("INVALID_OTP");
        }
        user.setEmailConfirmed(true);
        user.setOtpExpiration(null);
        userRepository.save(user);
        log.info("ActionLog.confirmEmail.success Email confirmed for email {}", confirmDto.getEmail());
    }

    private UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> {
                    log.error("ActionLog.confirmEmail.error User not found for email {}", email);
                    return new ResourceNotFoundException("User not found");
                }
        );

    }

    public Page<MemberDto> searchUsers(String fullName, String country, Pageable pageable) {
        log.info("ActionLog.searchUsers.start fullName {}, country {}", fullName, country);
        Specification<UserEntity> spec = Specification.where(UserSpecification.isEmailConfirmed())
                .and(UserSpecification.hasCountry(country))
                .and(UserSpecification.hasFullName(fullName));

        Page<UserEntity> userEntities = userRepository.findAll(spec, pageable);
        log.info("ActionLog.searchUsers.end fullName {}, country {}", fullName, country);
        return userEntities.map(UserMapper.INSTANCE::mapUserEntityToMemberDto);
    }

    public MemberInfoDto getUserById(Long id) {
        return UserMapper.INSTANCE.mapUserEntityToMemberInfoDto(userRepository.findById(id).orElseThrow(() ->
        {

            log.error("ActionLog.getUserById.error user not found with id {}", id);
            return new ResourceNotFoundException("MEMBER_NOT_FOUND");
        }));
    }

    public void updateUser(MemberUpdateDto memberUpdateDto, Long id) {
        var user = userRepository.findById(id).orElseThrow(() ->
        {
            log.error("ActionLog.getUserById.error user not found with id {}", id);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        UserEntity userEntity = UserMapper.INSTANCE.updateMember(user, memberUpdateDto);
        userEntity.setNotCompletedFields(0);
        String[] fields = memberUpdateDto.toString().split(",");
        for (String field : fields) {
            if (field.contains("null")) {
                userEntity.setNotCompletedFields(userEntity.getNotCompletedFields() + 1);
            }
        }
        userRepository.save(userEntity);

    }

    public void deleteUser(Long id) {
        log.info("ActionLog.delete.start id {}", id);
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with " + id);
        }
        userRepository.deleteById(id);
        log.info("ActionLog.delete.end id {}", id);
    }

    private String formatMessage(String message, String... values) {
        return MessageFormat.format(message, (Object[]) values);
    }
}

