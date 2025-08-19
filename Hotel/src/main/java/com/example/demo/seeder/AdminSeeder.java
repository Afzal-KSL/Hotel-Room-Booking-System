package com.example.demo.seeder;

import com.example.demo.model.Role;
import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if an ADMIN user already exists to avoid creating duplicates on every restart
        if (userRepository.findByRole(Role.ADMIN).isEmpty()) {
            UserModel admin = new UserModel();
            admin.setUsername("kslafzal@gmail.com");
            admin.setPassword(passwordEncoder.encode("johnwick")); // Use a strong password
            admin.setRole(Role.ADMIN);
            
            userRepository.save(admin);
            System.out.println(">>>>>>>>>> Created ADMIN user <<<<<<<<<<");
        }
    }
}