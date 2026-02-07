package com.issueflow.db;

import com.issueflow.modal.User;
import com.issueflow.modal.UserRole;
import com.issueflow.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitialSetup implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public InitialSetup(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if(!userRepository.existsByEmail("sys@admin.com")){
            userRepository.save(new User("sys@admin.com", "System User", encoder.encode("sys12"), UserRole.SYSTEM_ADMIN));
        }
    }
}
