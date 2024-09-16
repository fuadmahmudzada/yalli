package org.yalli.wah;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.yalli.wah.models.Role;
import org.yalli.wah.models.User;
import org.yalli.wah.repositories.RoleRepository;
import org.yalli.wah.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initRoles();
        initAdminUser();
    }

    private void initRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.saveAll(Arrays.asList(
                    new Role(null, "USER", new ArrayList<>()),
                    new Role(null, "ADMIN", new ArrayList<>())
            ));
        }
    }

    private void initAdminUser() {
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));

            User adminUser = new User();
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword(passwordEncoder.encode("adminpassword"));
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");
            adminUser.setEmailConfirmed(true);
            adminUser.setRoles(Collections.singletonList(adminRole));

            userRepository.save(adminUser);
            System.out.println("Admin user created: {}"+ adminUser.getEmail());
        } else {
            System.out.println("Admin user already exists");
        }
    }
}