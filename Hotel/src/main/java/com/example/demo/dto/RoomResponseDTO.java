package com.example.demo.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDTO {
    private int roomId;
    private int roomTypeId;
    private int floor;
    private int number;
    private String name;
    private boolean isAvailable;
    private byte[] image;
}