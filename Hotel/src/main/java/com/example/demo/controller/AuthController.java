package com.example.demo.controller;

import com.example.demo.model.UserModel;
import com.example.demo.model.Role;
import com.example.demo.service.UserService;
import com.example.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserModel user) {
        try {
            user.setRole(Role.GUEST);
            UserModel created = userService.registerUser(user);
            created.setPassword(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "User registered successfully",
                "user", created
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "error", "Registration failed",
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Registration failed",
                "message", "An unexpected error occurred"
            ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        try {
            if (userService.authenticate(username, password)) {
                UserModel user = userService.findByUsername(username).get();
                String token = jwtUtil.generateToken(username, user.getRole().name());
                
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("username", user.getUsername());
                response.put("role", user.getRole().name());
                if (user.getRole() == Role.GUEST) {
                    response.put("hasProfile", user.getGuest() != null);
                }
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", "Authentication failed",
                    "message", "Invalid username or password"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Login failed",
                "message", "An unexpected error occurred"
            ));
        }
    }
    
    @GetMapping("/users/role/{role}")
    public ResponseEntity<?> getUsersByRole(@PathVariable Role role) {
        try {
            return ResponseEntity.ok(userService.getUsersByRole(role));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Failed to fetch users",
                "message", e.getMessage()
            ));
        }
    }
}