package com.example.demo.model;

//import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password should not be empty")
//    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // GUEST, STAFF, ADMIN

    @OneToOne
    @JoinColumn(name = "guest_id", nullable = true)
    private GuestModel guest;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = true)
    private HotelModel hotel;
}