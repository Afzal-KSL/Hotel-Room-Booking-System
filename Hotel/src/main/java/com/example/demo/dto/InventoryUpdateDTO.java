package com.example.demo.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class InventoryUpdateDTO {
    
    @NotNull(message = "Hotel ID is required")
    private Integer hotelId;
    
    @NotNull(message = "Room Type ID is required")
    private Integer roomTypeId;
    
    @NotNull(message = "Date is required")
    private LocalDate date;
    
    @NotNull(message = "Count is required")
    @Positive(message = "Count must be positive")
    private Integer count;
}