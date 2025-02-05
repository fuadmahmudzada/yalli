package org.yalli.wah.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.yalli.wah.dao.entity.UserEntity;
import org.yalli.wah.dao.repository.UserRepository;
import org.yalli.wah.model.exception.ExcessivePasswordResetAttemptsException;
import org.yalli.wah.mapper.ProfileMapper;
import org.yalli.wah.mapper.UserMapper;
import org.yalli.wah.model.dto.*;
import org.yalli.wah.model.exception.InvalidInputException;
import org.yalli.wah.model.exception.InvalidOtpException;
import org.yalli.wah.model.exception.PermissionException;
import org.yalli.wah.model.exception.ResourceNotFoundException;

import org.yalli.wah.util.TokenUtil;
import org.yalli.wah.dao.specification.UserSpecification;

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
    private final TokenUtil tokenUtil;

    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<LoginResponseDto> login(Authentication authentication){

        log.info("ActionLog.login.start email {}", authentication.getName());


        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());

        if(user.isPresent() && user.get().getGoogleId()!=null){
            throw new InvalidInputException("USER_ALREADY_REGISTERED_WITH_GOOGLE");
        }

        UserEntity userEntity = user.orElseThrow(  () -> {
            log.error("ActionLog.login.error User not found for email {}", authentication.getName());
            return new ResourceNotFoundException("User not found");
        });

        userEntity.setAccessToken(tokenUtil.generateToken());
        userEntity.setTokenExpire(LocalDateTime.now().plusMinutes(30));
        if (!userEntity.isEmailConfirmed()) {
            log.info("ActionLog.login.error email {} not confirmed", authentication.getName());
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
        log.info("ActionLog.login.end email {}", authentication.getName());
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setAccessToken(userEntity.getAccessToken());
        loginResponseDto.setFullName(userEntity.getFullName());
        loginResponseDto.setCountry(userEntity.getCountry());
        loginResponseDto.setImage(userEntity.getProfilePictureUrl());
        loginResponseDto.setId(userEntity.getId());
//        return new HashMap<>() {{
//            put("accessToken", userEntity.getAccessToken());
//            put("fullName", userEntity.getFullName());
//            put("country", userEntity.getCountry());
//            put("image", userEntity.getProfilePictureUrl());
//            put("id", String.valueOf(userEntity.getId()));
//        }};
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDto);
    }

    public ResponseEntity<LoginResponseDto> googleLogin(Authentication authentication){
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> userAttributes =  oAuth2User.getAttributes();
        String email = (String)userAttributes.get("email");
        Optional<UserEntity> user = userRepository.findByEmail(email);


        if(user.isPresent() && user.get().getGoogleId()==null){
            throw new InvalidInputException("USER_LOGGED_IN_WITH_EMAIL");
        }

        if(!user.isPresent()){
            saveGoogleUser(authentication, userAttributes);
        }
        UserEntity userEntity = getUserByEmail(email);

        userEntity.setFullName((String) userAttributes.get("name"));
        userEntity.setEmail(email);
        userEntity.setGoogleId(authentication.getName());
        userEntity.setAccessToken(tokenUtil.generateToken());
        userEntity.setNotCompletedFields(0);
        MemberUpdateDto memberUpdateDto = ProfileMapper.INSTANCE.toMemberUpdateDto(userEntity);
        String[] fields = memberUpdateDto.toString().split(",");
        for (String field : fields) {
            if (field.contains("null")) {
                userEntity.setNotCompletedFields(userEntity.getNotCompletedFields() + 1);
            }
        }
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setAccessToken(userEntity.getAccessToken());
        loginResponseDto.setFullName(userEntity.getFullName());
        loginResponseDto.setCountry(userEntity.getCountry());
        loginResponseDto.setImage(userEntity.getProfilePictureUrl());
        loginResponseDto.setId(userEntity.getId());

        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDto);


    }

    public void saveGoogleUser(Authentication authentication, Map<?, ?> attributes){
        UserEntity userEntity = new UserEntity();
        userEntity.setGoogleId(authentication.getName());
        userEntity.setFullName((String) attributes.get("name"));
        userEntity.setEmail((String) attributes.get("email"));
        userEntity.setEmailConfirmed((boolean) attributes.get("email_verified"));
        userEntity.setResetRequests((byte)0);
        userEntity.setRole("user");
        userRepository.save(userEntity);
    }

//    public void processOAuthPostLogin(OAuth2AuthenticationToken authToken) {
//        if (authToken == null) {
//            log.error("Null OAuth2AuthenticationToken received");
//            throw new IllegalArgumentException("Authentication token cannot be null");
//        }
//
//        OAuth2User oauth2User = authToken.getPrincipal();
//        String email = oauth2User.getAttribute("email");
//
//        if (email == null) {
//            log.error("No email found in OAuth2 user attributes");
//            throw new IllegalStateException("Email is required for OAuth login");
//        }
//
//        UserEntity userEntity = userRepository.findByEmail(email)
//                .orElseGet(() -> createNewUserFromOAuth(oauth2User));
//
//        // Update user details if needed
//        userEntity.setFullName(oauth2User.getAttribute("name"));
//        userRepository.save(userEntity);
//
//        // Set authentication in SecurityContext
//        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userEntity.getRole()));
//        Authentication authentication = new UsernamePasswordAuthenticationToken(
//                userEntity.getEmail(),
//                null,
//                authorities
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
//
//
//
//    private UserEntity createNewUserFromOAuth(OAuth2User oauth2User) {
//        // Null checks for safety
//        if (oauth2User == null) {
//            throw new IllegalArgumentException("OAuth2 user cannot be null");
//        }
//
//        Map<String, Object> attributes = oauth2User.getAttributes();
//
//        UserEntity user = new UserEntity();
//        user.setEmail(
//                Optional.ofNullable((String) attributes.get("email"))
//                        .orElseThrow(() -> new IllegalStateException("Email is required for OAuth registration"))
//        );
//
//        user.setFullName(
//                Optional.ofNullable((String) attributes.get("name"))
//                        .orElse("Unknown User")
//        );
//
//        // Optional: Set profile picture if available
//        String profilePictureUrl = (String) attributes.get("picture");
//        if (profilePictureUrl != null) {
//            user.setProfilePictureUrl(profilePictureUrl);
//        }
//
//        // Confirmed via OAuth provider
//        user.setEmailConfirmed(true);
//
//        // Generate a secure random password
//        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
//
//        // Optional: Set default role or derive from OAuth provider
//        user.setRole("ROLE_USER");
//
//
//
//        return userRepository.save(user);
//    }

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
        UserEntity userEntity = userRepository.findByEmail(registerDto.getEmail()).map((user) -> {
            if (user.isEmailConfirmed() && user.getGoogleId()!=null) {
                throw new InvalidInputException("USER_ALREADY_REGISTERED_WITH_GOOGLE");
            } else if (user.isEmailConfirmed()) {
                throw new InvalidInputException("EMAIL_ALREADY_EXISTS");

            }
            return user;
        }).orElse(new UserEntity());
        userEntity = UserMapper.INSTANCE.mapRegisterDtoToUser(registerDto, userEntity);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRole("user");
        userEntity.setResetRequests((byte)0);

        //send otp
        processOtp(userEntity);

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

        if (user.getResetRequests() == 0) {
            user.setResetRequestBanExpiration(LocalDateTime.now().plusMinutes(5));
        }

        if (user.getResetRequestBanExpiration().plusMinutes(5).isBefore(LocalDateTime.now())) {
            user.setResetRequests((byte) 0);
        }

        if (user.getResetRequests() > 3) {
            throw new ExcessivePasswordResetAttemptsException((int) user.getResetRequests(), 3);
        }


        user.setResetRequests((byte) (user.getResetRequests() + (byte) 1));


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

        if (passwordEncoder.matches(passwordResetDto.getNewPassword(), user.getPassword())) {
            log.info("ActionLog.resetPassword.error new password is same as old one email {}", passwordResetDto.getEmail());
            throw new InvalidInputException("SAME_WITH_OLD_PASSWORD");
        }

        user.setPassword(passwordEncoder.encode(passwordResetDto.getNewPassword()));
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
                    log.error("ActionLog.getUserByEmail.error User not found for email {}", email);
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
            throw new ResourceNotFoundException("User not found with " + id);
        }
        userRepository.deleteById(id);
        log.info("ActionLog.delete.end id {}", id);
    }

    public void processOtp(UserEntity userEntity) {
        log.info("ActionLog.sendRegisterOtp.start email {}", userEntity.getEmail());
        String firstOtp = userEntity.getOtp();
        String otp = generateOtp();
        userEntity.setOtp(otp);
        userEntity.setOtpExpiration(LocalDateTime.now().plusSeconds(60));
        emailService.sendMail(userEntity.getEmail(), CONFIRMATION.getSubject(), formatMessage(CONFIRMATION.getBody(), otp));
        if (firstOtp != null) {
            userRepository.save(userEntity);
        }
        log.info("ActionLog.sendRegisterOtp.end email {}", userEntity.getEmail());
    }

    public void resendRegisterOtp(String email) {
        UserEntity userEntity = getUserByEmail(email);
        processOtp(userEntity);
    }

    private String formatMessage(String message, String... values) {
        return MessageFormat.format(message, (Object[]) values);
    }

}

