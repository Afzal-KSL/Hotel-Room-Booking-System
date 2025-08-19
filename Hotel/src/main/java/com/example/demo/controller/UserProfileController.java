package com.example.demo.controller;

import com.example.demo.model.UserModel;
import com.example.demo.service.UserService;
import com.example.demo.service.GuestService;
import com.example.demo.model.GuestModel;
import com.example.demo.model.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private GuestService guestService;

    @GetMapping
    public ResponseEntity<?> getProfile(Authentication authentication) {
        String username = authentication.getName();
        UserModel user = userService.findByUsername(username).orElse(null);
        
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/guest")
    public ResponseEntity<?> createGuestProfile(
            @RequestBody GuestModel guestData, 
            Authentication authentication) {
        
        String username = authentication.getName();
        UserModel user = userService.findByUsername(username).orElse(null);
        
        if (user == null || user.getRole() != Role.GUEST) {
            return ResponseEntity.badRequest().body("Invalid user or role");
        }

        if (user.getGuest() != null) {
            return ResponseEntity.badRequest().body("Profile already exists");
        }

        guestData.setEmail(username);
        GuestModel savedGuest = guestService.addGuest(guestData);
        
        user.setGuest(savedGuest);
        userService.updateUser(user);

        return ResponseEntity.ok(savedGuest);
    }
}