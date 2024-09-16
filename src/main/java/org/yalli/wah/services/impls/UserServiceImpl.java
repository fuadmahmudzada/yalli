package org.yalli.wah.services.impls;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yalli.wah.dtos.RegisterDto;
import org.yalli.wah.dtos.UserDto;
import org.yalli.wah.models.Role;
import org.yalli.wah.models.User;
import org.yalli.wah.repositories.RoleRepository;
import org.yalli.wah.repositories.UserRepository;
import org.yalli.wah.services.EmailService;
import org.yalli.wah.services.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
        String token = generateConfirmationToken();

        User newUser = new User();
        newUser.setEmail(register.getEmail());
        newUser.setFirstName(register.getFirstName());
        newUser.setLastName(register.getLastName());
        newUser.setEmailConfirmed(false);
        newUser.setConfirmationToken(token);
        newUser.setPassword(hashedPassword);


        User savedUser = userRepository.save(newUser);

        // Send confirmation email
        emailService.sendConfirmationEmail(register.getEmail(), token);

        return modelMapper.map(savedUser, UserDto.class);
    }
    @Override
    public boolean confirmEmail(String email, String token) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getConfirmationToken().equals(token)) {
                user.setEmailConfirmed(true);
                user.setConfirmationToken(null);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    private String generateConfirmationToken() {
        return UUID.randomUUID().toString();
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

}
