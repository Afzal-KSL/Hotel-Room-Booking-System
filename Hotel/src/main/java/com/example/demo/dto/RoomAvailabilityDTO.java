package com.example.demo.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomAvailabilityDTO {
    private int roomId;
    private int roomTypeId;
    private int floor;
    private int number;
    private String name;
    private byte[] image;
    private boolean available;
    private int totalInventory;
    private int totalReserved;
    private int rate;
}