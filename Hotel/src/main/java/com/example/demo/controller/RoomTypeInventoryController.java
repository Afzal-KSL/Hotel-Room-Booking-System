package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.InventoryUpdateDTO;
import com.example.demo.model.RoomTypeInventoryModel;
import com.example.demo.service.RoomTypeInventoryService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/inventory")
public class RoomTypeInventoryController {

    @Autowired
    private RoomTypeInventoryService service;

    @GetMapping
    public ResponseEntity<RoomTypeInventoryModel> getInventory(
            @RequestParam int hotelId,
            @RequestParam int roomTypeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        RoomTypeInventoryModel inventory = service.getCapacity(hotelId, roomTypeId, date);
        return inventory != null ? ResponseEntity.ok(inventory) : ResponseEntity.notFound().build();
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RoomTypeInventoryModel>> getInventoryByHotel(@PathVariable int hotelId) {
        List<RoomTypeInventoryModel> inventory = service.getInventoryByHotel(hotelId);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/hotel/{hotelId}/room-type/{roomTypeId}")
    public ResponseEntity<List<RoomTypeInventoryModel>> getInventoryByHotelAndRoomType(
            @PathVariable int hotelId,
            @PathVariable int roomTypeId) {
        List<RoomTypeInventoryModel> inventory = service.getInventoryByHotelAndRoomType(hotelId, roomTypeId);
        return ResponseEntity.ok(inventory);
    }

    @PostMapping
    public ResponseEntity<RoomTypeInventoryModel> createInventory(@Valid @RequestBody RoomTypeInventoryModel inventory) {
        RoomTypeInventoryModel created = service.addCapacity(inventory);
        return ResponseEntity.ok(created);
    }

    @PutMapping
    public ResponseEntity<RoomTypeInventoryModel> updateInventory(@Valid @RequestBody RoomTypeInventoryModel inventory) {
        RoomTypeInventoryModel updated = service.updateInventory(inventory);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PutMapping("/reserve")
    public ResponseEntity<?> reserveRooms(@Valid @RequestBody InventoryUpdateDTO dto) {
        RoomTypeInventoryModel updated = service.reserveRooms(
            dto.getHotelId(), 
            dto.getRoomTypeId(), 
            dto.getDate(), 
            dto.getCount()
        );
        
        if (updated == null) {
            return ResponseEntity.badRequest().body("Not enough rooms available for reservation");
        }
        
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/release")
    public ResponseEntity<?> releaseRooms(@Valid @RequestBody InventoryUpdateDTO dto) {
        RoomTypeInventoryModel updated = service.releaseRooms(
            dto.getHotelId(), 
            dto.getRoomTypeId(), 
            dto.getDate(), 
            dto.getCount()
        );
        
        if (updated == null) {
            return ResponseEntity.badRequest().body("Cannot release more rooms than currently reserved");
        }
        
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/capacity")
    public ResponseEntity<?> updateCapacity(@Valid @RequestBody InventoryUpdateDTO dto) {
        RoomTypeInventoryModel updated = service.updateCapacity(
            dto.getHotelId(), 
            dto.getRoomTypeId(), 
            dto.getDate(), 
            dto.getCount()
        );
        
        if (updated == null) {
            return ResponseEntity.badRequest().body("Cannot set capacity below current reservations");
        }
        
        return ResponseEntity.ok(updated);
    }
}