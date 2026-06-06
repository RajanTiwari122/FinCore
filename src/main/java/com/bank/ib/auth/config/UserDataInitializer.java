package com.bank.ib.auth.config;

import com.bank.ib.auth.entity.User;
import com.bank.ib.auth.repository.UserRepository;
import com.bank.ib.enums.Role;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataInitializer(UserRepository userRepository,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {

        if (userRepository.count() == 0) {

            createUser("admin", "admin123", Role.ADMIN);
            createUser("trader1", "trader123", Role.TRADER);
            createUser("risk1", "risk123", Role.RISK);
            createUser("ops1", "ops123", Role.OPS);
        }
    }

    private void createUser(String username, String rawPassword, Role role) {

        User user = new User();
        user.setUsername(username);
        user.setFirstName("Admin");
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setEnabled(true);

        userRepository.save(user);
    }
}

