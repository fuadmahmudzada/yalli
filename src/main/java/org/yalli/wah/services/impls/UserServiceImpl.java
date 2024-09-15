package org.yalli.wah.services.impls;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yalli.wah.dtos.RegisterDto;
import org.yalli.wah.dtos.UserDto;
import org.yalli.wah.models.User;
import org.yalli.wah.repositories.UserRepository;
import org.yalli.wah.services.EmailService;
import org.yalli.wah.services.UserService;

import java.util.Optional;
import java.util.UUID;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Now autowire should work

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

        // Save the user in the database
        User savedUser = userRepository.save(newUser);

        // Send confirmation email
        emailService.sendConfirmationEmail(register.getEmail(), token);

        return modelMapper.map(savedUser, UserDto.class);
    }
    @Override
    public boolean confirmEmail(String email, String token) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();  // Unwrap Optional
            if (user.getConfirmationToken().equals(token)) {
                user.setEmailConfirmed(true);
                user.setConfirmationToken(null);  // Clear token after confirmation
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    private String generateConfirmationToken() {
        return UUID.randomUUID().toString();
    }


}
