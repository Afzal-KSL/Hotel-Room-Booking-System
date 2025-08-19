package com.example.demo.service;

import com.example.demo.model.UserModel;
import com.example.demo.model.Role;
import com.example.demo.repository.UserRepository;
import java.util.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public UserModel updateUser(UserModel user) {
        return repo.save(user);
    }

    public UserModel registerUser(UserModel user) {
        Optional<UserModel> existingUser = repo.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User with email " + user.getUsername() + " already exists");
        }

        if (user.getRole() == Role.GUEST && !user.getUsername().contains("@")) {
            throw new IllegalArgumentException("Guest username must be a valid email address");
        }  
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public Optional<UserModel> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public boolean authenticate(String username, String rawPassword) {
        Optional<UserModel> optionalUser = repo.findByUsername(username);
        return optionalUser.isPresent() &&
               passwordEncoder.matches(rawPassword, optionalUser.get().getPassword());
    }
    
    public List<UserModel> getUsersByRole(Role role) {
        return repo.findByRole(role);
    }
    
    public List<UserModel> getAllUsers() {
        return repo.findAll();
    }
    
    public void deleteUser(int id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        repo.deleteById(id);
    }
}