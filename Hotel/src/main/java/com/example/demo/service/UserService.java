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

    public UserModel registerUser(UserModel user) {
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
}