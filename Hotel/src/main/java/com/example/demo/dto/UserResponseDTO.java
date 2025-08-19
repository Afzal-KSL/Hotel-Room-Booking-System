package com.example.demo.dto;

import com.example.demo.model.Role;
import com.example.demo.model.UserModel;
import lombok.Data;

@Data
public class UserResponseDTO {
    private int userId;
    private String username;
    private Role role;
    private String guestFullName;
    private Integer guestId;

    public UserResponseDTO(UserModel user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.role = user.getRole();
        if (user.getGuest() != null) {
            this.guestId = user.getGuest().getGuestId();
            this.guestFullName = user.getGuest().getFirstName() + " " + user.getGuest().getLastName();
        } else {
            this.guestFullName = "N/A";
        }
    }
}