package com.example.demo.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationRequestDTO {

    @NotNull(message = "Hotel ID is required")
    private Integer hotelId;

    @NotNull(message = "Guest ID is required")
    private Integer guestId;

    @NotNull(message = "Room ID is required")
    private Integer roomId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;
}
