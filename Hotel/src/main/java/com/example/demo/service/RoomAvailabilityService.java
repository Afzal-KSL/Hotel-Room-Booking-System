package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.RoomModel;
import com.example.demo.model.RoomTypeInventoryModel;
import com.example.demo.model.RoomTypeRateModel;
import com.example.demo.dto.RoomAvailabilityDTO;

@Service
public class RoomAvailabilityService {
    
    @Autowired
    private RoomTypeInventoryService inventoryService;
    
    @Autowired
    private RoomTypeRateService rateService;
    
    public List<RoomAvailabilityDTO> getRoomAvailabilityForHotel(
            List<RoomModel> rooms, 
            int hotelId, 
            LocalDate checkDate) {
        
        return rooms.stream()
            .map(room -> {
                // Get inventory for this room type and date
                RoomTypeInventoryModel inventory = inventoryService.getCapacity(
                    hotelId, 
                    room.getRoomTypeId(), 
                    checkDate
                );
                
                // Get rate for this room type and date
                RoomTypeRateModel rate = rateService.getRate(
                    hotelId, 
                    room.getRoomTypeId(), 
                    checkDate
                );
                
                // Calculate actual availability
                boolean isInventoryAvailable = (inventory != null) && 
                    (inventory.getTotalInventory() > inventory.getTotalReserved());
                
                // Final availability = room is enabled AND has inventory
                boolean finalAvailability = room.isAvailable() && isInventoryAvailable;
                
                // Debug logging
                System.out.println("Room " + room.getName() + 
                    " (Type: " + room.getRoomTypeId() + ")" +
                    " - Room Available: " + room.isAvailable() +
                    " - Inventory: " + (inventory != null ? 
                        inventory.getTotalInventory() + "/" + inventory.getTotalReserved() : "null") +
                    " - Final: " + finalAvailability);
                
                return new RoomAvailabilityDTO(
                    room.getRoomId(),
                    room.getRoomTypeId(),
                    room.getFloor(),
                    room.getNumber(),
                    room.getName(),
                    room.getImage(),
                    finalAvailability,
                    inventory != null ? inventory.getTotalInventory() : 0,
                    inventory != null ? inventory.getTotalReserved() : 0,
                    rate != null ? rate.getRate() : 0
                );
            })
            .collect(Collectors.toList());
    }
}