package org.yalli.wah.services.impls;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yalli.wah.dtos.PasswordResetDto;
import org.yalli.wah.dtos.RegisterDto;
import org.yalli.wah.dtos.UserDto;
import org.yalli.wah.models.Role;
import org.yalli.wah.models.User;
import org.yalli.wah.payloads.ApiResponse;
import org.yalli.wah.repositories.RoleRepository;
import org.yalli.wah.repositories.UserRepository;
import org.yalli.wah.services.EmailService;
import org.yalli.wah.services.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmailService emailService;
    @Override
    public UserDto register(RegisterDto register) {
        Optional<User> existingUser = userRepository.findByEmail(register.getEmail());

        if (existingUser.isPresent()) {
            return null;  // Email already exists
        }

        if (!register.getPassword().equals(register.getPasswordRepeat())) {
            return null;  // Password mismatch
        }

        // Encrypt the password
        String hashedPassword = passwordEncoder.encode(register.getPassword());

        // Generate a 6-digit OTP token
        String otp = generateConfirmationToken();

        User newUser = new User();
        newUser.setEmail(register.getEmail());
        newUser.setFirstName(register.getFirstName());
        newUser.setLastName(register.getLastName());
        newUser.setEmailConfirmed(false);
        newUser.setConfirmationToken(otp);
        newUser.setPassword(hashedPassword);

        User savedUser = userRepository.save(newUser);

        // Send the OTP in the email
        emailService.sendConfirmationEmail(register.getEmail(), otp);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public boolean confirmEmail(String email, String otp) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getConfirmationToken().equals(otp)) {
                user.setEmailConfirmed(true);
                user.setConfirmationToken(null);  // Clear the OTP after confirmation
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }


    private String generateConfirmationToken() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);  // Generate a 6-digit OTP
        return String.valueOf(otp);
    }


    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(RegisterDto registerDto) {
        User newUser = modelMapper.map(registerDto, User.class);
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        newUser.setEmailConfirmed(true);  // Assume admin-created users are pre-confirmed


        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        newUser.setRoles(Collections.singletonList(userRole));

        User savedUser = userRepository.save(newUser);
        return modelMapper.map(savedUser, UserDto.class);
    }
    @Override
    public ApiResponse requestPasswordReset(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String otp = generateOtp();
            user.setOtp(otp);
            userRepository.save(user);

            // Send OTP to the user's email
            emailService.sendOtpEmail(user.getEmail(), otp);
            return new ApiResponse("OTP sent to email", true);
        }

        return new ApiResponse("Email not found", false);
    }

    // Step 2: Verify OTP
    @Override
    public ApiResponse verifyOtp(String email, String otp) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getOtp().equals(otp)) {
                return new ApiResponse("OTP is correct", true);
            }
        }

        return new ApiResponse("Invalid OTP", false);
    }

    // Step 3: Reset password
    @Override
    public ApiResponse resetPassword(PasswordResetDto passwordResetDto) {
        // Validate OTP first (if you already implemented OTP checking)

        if (!passwordResetDto.getNewPassword().equals(passwordResetDto.getNewPasswordRepeat())) {
            return new ApiResponse("Passwords do not match!", false);
        }

        Optional<User> userOptional = userRepository.findByEmail(passwordResetDto.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Reset the password
            String hashedPassword = passwordEncoder.encode(passwordResetDto.getNewPassword());
            user.setPassword(hashedPassword);
            userRepository.save(user);

            return new ApiResponse("Password reset successful", true);
        }

        return new ApiResponse("Invalid email or OTP", false);
    }

    // Utility to generate OTP
    private String generateOtp() {
        return String.valueOf(new Random().nextInt(999999));  // 6-digit OTP
    }
}
