package com.example.demo.controller;

import com.example.demo.dto.UserResponseDTO;
import com.example.demo.model.Role;
import com.example.demo.model.UserModel;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')") 
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers(@RequestParam(required = false) Role role) {
        List<UserModel> users = (role != null)
                ? userService.getUsersByRole(role)
                : userService.getAllUsers();

        List<UserResponseDTO> userDTOs = users.stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(userDTOs);
    }
    
    @PostMapping("/staff")
    public ResponseEntity<?> createStaff(@RequestBody UserModel staff) {
        try {
            staff.setRole(Role.STAFF);
            UserModel newStaff = userService.registerUser(staff);
            newStaff.setPassword(null); 
            return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDTO(newStaff));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User with ID " + id + " has been deleted."));
    }
}