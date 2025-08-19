package com.example.demo.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponseDTO {
    private int hotelId;
    private String name;
    private String address;
    private String location;
    private byte[] image;
    private List<RoomResponseDTO> rooms;
    private String checkDate;
    
    // Constructor without rooms for list view
    public HotelResponseDTO(int hotelId, String name, String address, String location, byte[] image) {
        this.hotelId = hotelId;
        this.name = name;
        this.address = address;
        this.location = location;
        this.image = image;
    }
}