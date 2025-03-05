package org.yalli.wah.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.yalli.wah.dao.entity.UserCoordinateEntity;
import org.yalli.wah.dao.entity.UserEntity;
import org.yalli.wah.dao.repository.UserCoordinateRepository;
import org.yalli.wah.dao.repository.UserRepository;
import org.yalli.wah.model.exception.ExcessivePasswordResetAttemptsException;
import org.yalli.wah.mapper.ProfileMapper;
import org.yalli.wah.mapper.UserMapper;
import org.yalli.wah.model.dto.*;
import org.yalli.wah.model.exception.InvalidInputException;
import org.yalli.wah.model.exception.InvalidOtpException;
import org.yalli.wah.model.exception.PermissionException;
import org.yalli.wah.model.exception.ResourceNotFoundException;

import org.yalli.wah.util.CoordinateUtil;
import org.yalli.wah.util.TokenUtil;
import org.yalli.wah.dao.specification.UserSpecification;

import java.io.IOException;
import java.sql.SQLOutput;
import java.text.MessageFormat;
import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.Random;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.yalli.wah.model.enums.EmailTemplate.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;

    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserCoordinateRepository userCoordinateRepository;

    private final GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);

    public ResponseEntity<LoginResponseDto> login(Authentication authentication) throws IOException, InterruptedException {
        log.info("ActionLog.login.start email {}", authentication.getName());


        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());

        if (user.isPresent() && user.get().getGoogleId() != null) {
            throw new InvalidInputException("USER_ALREADY_REGISTERED_WITH_GOOGLE");
        }

        UserEntity userEntity = user.orElseThrow(() -> {
            log.error("ActionLog.login.error User not found for email {}", authentication.getName());
            return new ResourceNotFoundException("User not found");
        });
        userEntity.setAccessToken(tokenUtil.generateToken());
        userEntity.setTokenExpire(LocalDateTime.now().plusMinutes(30));
        CoordinateDto coordinateDto = CoordinateUtil.findCoordinate(userEntity.getCity());
        UserCoordinateEntity userCoordinate = userEntity.getUserCoordinate();
        Coordinate[] coordinates = new Coordinate[]{new Coordinate(coordinateDto.getLng(), coordinateDto.getLat())};
        CoordinateSequence coordinateSequence = new CoordinateArraySequence(coordinates);
        Point point = factory.createPoint(coordinateSequence);
        if (userEntity.getUserCoordinate() == null) {
            userCoordinate = new UserCoordinateEntity(point, List.of(userEntity));
            userEntity.setUserCoordinate(userCoordinate);
        } else {
            userCoordinate.setLocation(point);
            userEntity.setUserCoordinate(userCoordinate);
        }

        userCoordinateRepository.save(userCoordinate);
        userEntity.setUserCoordinate(userCoordinate);

        if (!userEntity.isEmailConfirmed()) {
            log.info("ActionLog.login.error email {} not confirmed", authentication.getName());
            throw new InvalidInputException("EMAIL_NOT_CONFIRMED");

        }
        userRepository.save(userEntity);
        log.info("ActionLog.login.end email {}", authentication.getName());
        LoginResponseDto loginResponseDto = UserMapper.INSTANCE.loginResponseDto(userEntity);
//        return new HashMap<>() {{
//            put("accessToken", userEntity.getAccessToken());
//            put("fullName", userEntity.getFullName());
//            put("country", userEntity.getCountry());
//            put("image", userEntity.getProfilePictureUrl());
//            put("id", String.valueOf(userEntity.getId()));
//        }};
        loginResponseDto.setIsGoogleLogin(false);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDto);
    }

    private EmptyFieldsDto calcUserEmptyFields(UserEntity user) {
        log.info("ActionLog.calcUserEmptyFields.start with user email {} ", user.getEmail());
        ProfileCompleteDto profileCompleteDto = ProfileMapper.INSTANCE.profileCompleteDto(user);
        Map<String, String> userFields = new HashMap<>();
        userFields.put("BirthDate", String.valueOf(profileCompleteDto.getBirthDate()));
        userFields.put("ProfilePicture", String.valueOf(profileCompleteDto.getProfilePicture()));
        userFields.put("SocialMediaLinks", String.valueOf(profileCompleteDto.getSocialMediaLinks()));
        userFields.put("Experiences", String.valueOf(profileCompleteDto.getExperienceIds()));
        List<String> notCompletedFields = new ArrayList<>();
        userFields.forEach((key, value) -> {
            if (value.equals("null") || value.equals("[]")) {
                notCompletedFields.add(key);
            }
        });
        float completionPercent = ((5f - notCompletedFields.size() )/ 5f) * 100;
        log.info("ActionLog.calcUserEmptyFields.end with user email {} ", user.getEmail());
        return new EmptyFieldsDto(completionPercent, notCompletedFields);
    }

    public ResponseEntity<LoginResponseDto> googleLogin(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> userAttributes = oAuth2User.getAttributes();
        String email = (String) userAttributes.get("email");
        Optional<UserEntity> user = userRepository.findByEmail(email);
        log.info("ActionLog.googleLogin.start with user email {} ", email);


        if (user.isPresent() && user.get().getGoogleId() == null) {
            throw new InvalidInputException("USER_LOGGED_IN_WITH_EMAIL");
        }

        if (!user.isPresent()) {
            saveGoogleUser(authentication, userAttributes);
        }
        UserEntity userEntity = getUserByEmail(email);

        userEntity.setFullName((String) userAttributes.get("name"));
        userEntity.setEmail(email);
        userEntity.setGoogleId(authentication.getName());
        userEntity.setAccessToken(tokenUtil.generateToken());

        LoginResponseDto loginResponseDto = UserMapper.INSTANCE.loginResponseDto(userEntity);
        loginResponseDto.setIsGoogleLogin(true);
        log.info("ActionLog.googleLogin.end with user email {} ", email);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDto);
    }

    public void saveGoogleUser(Authentication authentication, Map<?, ?> attributes) {
        UserEntity userEntity = new UserEntity();
        userEntity.setGoogleId(authentication.getName());
        userEntity.setFullName((String) attributes.get("name"));
        userEntity.setEmail((String) attributes.get("email"));
        userEntity.setEmailConfirmed((boolean) attributes.get("email_verified"));
        userEntity.setResetRequests((byte) 0);
        userEntity.setRole("user");
        userRepository.save(userEntity);
    }

    public void addUserProvidedGoogleLoginInfo(String country, String city, Authentication authentication) throws IOException, InterruptedException {
        UserEntity userEntity = userRepository.findByEmail(authentication.getName()).orElseThrow(() ->
                new ResourceNotFoundException("User not found with email" + authentication.getName()));
        userEntity.setCountry(country);
        userEntity.setCity(city);
        CoordinateDto coordinateDto = CoordinateUtil.findCoordinate(city);
        Coordinate[] coordinates = new Coordinate[]{new Coordinate(coordinateDto.getLng(), coordinateDto.getLat())};
        CoordinateSequence coordinateSequence = new CoordinateArraySequence(coordinates);
        Point point = factory.createPoint(coordinateSequence);
        userEntity.setUserCoordinate(new UserCoordinateEntity(point, List.of(userEntity)));
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

    public void register(RegisterDto registerDto) throws IOException, InterruptedException {
        log.info("ActionLog.register.start email {}", registerDto.getEmail());
        UserEntity userEntity = userRepository.findByEmail(registerDto.getEmail()).map((user) -> {
            if (user.isEmailConfirmed() && user.getGoogleId() != null) {
                throw new InvalidInputException("USER_ALREADY_REGISTERED_WITH_GOOGLE");
            } else if (user.isEmailConfirmed()) {
                throw new InvalidInputException("EMAIL_ALREADY_EXISTS");

            }
            return user;
        }).orElse(new UserEntity());
        userEntity = UserMapper.INSTANCE.mapRegisterDtoToUser(registerDto, userEntity);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRole("user");
        userEntity.setResetRequests((byte) 0);
        CoordinateDto coordinateDto = CoordinateUtil.findCoordinate(registerDto.getCity());
        Coordinate[] coordinates = new Coordinate[]{new Coordinate(coordinateDto.getLng(), coordinateDto.getLat())};
        CoordinateSequence coordinateSequence = new CoordinateArraySequence(coordinates);
        Point point = factory.createPoint(coordinateSequence);
        UserCoordinateEntity userCoordinateEntity =  new UserCoordinateEntity(point, List.of(userEntity));
        userCoordinateRepository.save(userCoordinateEntity);
        userEntity.setUserCoordinate(userCoordinateEntity);

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

    public Page<MemberDto> searchUsers(UserSearchDto userSearchDto, Pageable pageable) {
        String fullName = userSearchDto.getFullName();
        List<String> country = userSearchDto.getCountry();
        List<String> city = userSearchDto.getCity();
        log.info("ActionLog.searchUsers.start fullName {}, country {}, city {}", fullName, country, city);
        Specification<UserEntity> spec = Specification.where(UserSpecification.isEmailConfirmed());
        if ((country != null && !country.isEmpty()) || (city != null && !city.isEmpty())) {
            spec = spec.and(UserSpecification.hasCountryOrCity(country, city));
        }
        if (fullName != null) {
            spec = spec.and(UserSpecification.hasFullName(fullName));
        }
//                .and()
//                .and(UserSpecification.hasFullName(fullName));


        Page<UserEntity> userEntities = userRepository.findAll(spec, pageable);
        userRepository.findAll(spec, pageable);
        log.info("ActionLog.searchUsers.end fullName {}, country {}", fullName, country);
        return userEntities.map(UserMapper.INSTANCE::mapUserEntityToMemberDto);
    }

    public MemberMapDto getUsersOnMap(CoordinateDto coordinateDto) throws IOException, InterruptedException {
        Map<String, String> locationMap = CoordinateUtil.findToponymByCoordinate(coordinateDto);
        String country = locationMap.keySet().stream().findFirst().get();
        String city = locationMap.values().stream().findFirst().get();

        log.info("ActionLog.getUsersOnMap.start country {}, city {}", country, city);
        Specification<UserEntity> specificationFindNotNull = Specification.where(UserSpecification.isEmailConfirmed())
                .and(UserSpecification.hasCountryOrCity(List.of(country), List.of(city)))
                .and(UserSpecification.isProfilePictureNotNull());
        Specification<UserEntity> specificationFindAllEntities = Specification.where(UserSpecification.isEmailConfirmed())
                .and(UserSpecification.hasCountryOrCity(List.of(country), List.of(city)));
        Page<UserEntity> userEntities = userRepository.findAll(specificationFindNotNull, PageRequest.of(0, 3));
        List<String> profilePictures = userEntities.stream().map(UserEntity::getProfilePictureUrl).toList();
        long count = userRepository.count(specificationFindAllEntities);
        log.info("ActionLog.getUsersOnMap.end country {}, city {}", country, city);
        return new MemberMapDto(profilePictures, (int) count);
//burda casting olmalimi yuxarida map ve onun yuxarisindaki pagein ferqli tipine cevirme
    }

    public List<CoordinateDto> getAllCoordinates(Polygon polygon) {
//        List<Coordinate> coordinates = new ArrayList<>();
//        System.out.println(geometryDto.getGeoPolygon().getCoordinates());
//        borders.forEach(x->coordinates.add(new Coordinate(Double.parseDouble(x.split(" ")[0]),Double.parseDouble(x.split(" ")[1]))));
//        Polygon polygon =  factory.createPolygon(borders.toArray(Coordinate[]::new));
        List<UserCoordinateEntity> list = userCoordinateRepository.findAllWithinGivenPolygon(polygon);
        List<CoordinateDto> coordinateDtoList = new ArrayList<>();
        list.stream().forEach(System.out::println);
        Float xValue = 0f;
        Float yValue = 0f;

        if (list != null && !list.isEmpty()) {
            System.out.println("location X " + list.getFirst().getLocation().getX());
            System.out.println("location Y " + list.getFirst().getLocation().getY());
            list.stream().forEach(x -> coordinateDtoList.add(new CoordinateDto
                    ((float) x.getLocation().getX(), (float) x.getLocation().getY())));
        }
        return coordinateDtoList;
    }

    public MemberInfoDto getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() ->
        {

            log.error("ActionLog.getUserById.error user not found with id {}", id);
            return new ResourceNotFoundException("MEMBER_NOT_FOUND");
        });
        EmptyFieldsDto emptyFieldsDto = calcUserEmptyFields(userEntity);
        return UserMapper.INSTANCE.mapUserEntityToMemberInfoDto(userEntity, emptyFieldsDto.getNotCompletedFields(), emptyFieldsDto.getCompletionPercent());
    }

    public void updateUser(MemberUpdateDto memberUpdateDto, Long id) {
        var user = userRepository.findById(id).orElseThrow(() ->
        {
            log.error("ActionLog.updateUser.error user not found with id {}", id);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        UserEntity userEntity = UserMapper.INSTANCE.updateMember(user, memberUpdateDto);
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

