package com.springsecurity.bootstrap;

import com.springsecurity.model.User;
import com.springsecurity.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() == 0) {
            createUser("taha", "admin123", "ROLE_ADMIN", "admin@gmail.com", "0600000001");
            createUser("sami", "manager123", "ROLE_MANAGER", "manager@gmail.com", "0600000002");
            createUser("ahmed", "user123", "ROLE_SUPERVISOR", "user@yahoo.com", "0600000003");
            createUser("youcef", "auditor123", "ROLE_AUDITOR", "auditor@yahoo.com", "0600000004");
            createUser("sara", "hr123", "ROLE_HR", "hr@outlook.com", "0600000005");
            createUser("amina", "support123", "ROLE_SUPPORT", "support@outlook.com", "0600000006");
            createUser("guest", "guest123", "ROLE_GUEST", "guest@example.com", "0600000007");

            log.info("✅ 7 users saved to the database.");
        }
    }

    private void createUser(String username, String password, String role, String email, String phone) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setEmail(email);
        user.setPhone(phone);
        userRepository.save(user);
    }
}
