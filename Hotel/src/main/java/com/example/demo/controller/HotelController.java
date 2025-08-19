package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.HotelResponseDTO;
import com.example.demo.model.HotelModel;
import com.example.demo.service.HotelService;
import com.example.demo.dto.RoomResponseDTO;

@RestController
@RequestMapping("/hotels")
@CrossOrigin(origins = "http://localhost:5173")
public class HotelController {

	@Autowired
	private HotelService service;
	
	@GetMapping
    public ResponseEntity<?> getAllHotels(){
        try {
            List<HotelModel> hotels = service.getAllHotels();
            
            // Convert to DTOs (without rooms for performance)
            List<HotelResponseDTO> hotelDTOs = hotels.stream()
                .map(hotel -> new HotelResponseDTO(
                    hotel.getHotelId(),
                    hotel.getName(),
                    hotel.getAddress(),
                    hotel.getLocation(),
                    hotel.getImage()
                ))
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(hotelDTOs);
        } catch (Exception e) {
            System.err.println("Error in getAllHotels: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error fetching hotels", "message", e.getMessage()));
        }
    }
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getHotel(
	        @PathVariable int id,
	        @RequestParam(required = false) String checkDate) {
	    try {
	        System.out.println("=== Getting Hotel " + id + " for date: " + checkDate + " ===");
	        
	        LocalDate dateToCheck = checkDate != null ? 
	            LocalDate.parse(checkDate) : LocalDate.now();
	        
	        // Use the method that checks inventory
	        HotelModel hotel = service.getHotelWithRoomsAndCheckInventory(id, dateToCheck);
	        
	        if (hotel == null) {
	            return ResponseEntity.notFound().build();
	        }
	        
	        // ✅ Create room DTOs first
	        List<RoomResponseDTO> roomDTOs = hotel.getRooms() != null ? 
	            hotel.getRooms().stream()
	                .map(room -> new RoomResponseDTO(
	                    room.getRoomId(),
	                    room.getRoomTypeId(),
	                    room.getFloor(),
	                    room.getNumber(),
	                    room.getName(),
	                    room.isAvailable(), // This now includes inventory check
	                    room.getImage()
	                ))
	                .collect(Collectors.toList()) : new ArrayList<>();
	        
	        // ✅ Create hotel DTO using the constructor that EXISTS in your code
	        HotelResponseDTO hotelDTO = new HotelResponseDTO(
	            hotel.getHotelId(),
	            hotel.getName(),
	            hotel.getAddress(),
	            hotel.getLocation(),
	            hotel.getImage()
	        );
	        
	        hotelDTO.setRooms(roomDTOs);
	        
	        System.out.println("Hotel: " + hotel.getName() + " with " + 
	            roomDTOs.size() + " rooms returned");
	        
	        return ResponseEntity.ok(hotelDTO);
	        
	    } catch (Exception e) {
	        System.err.println("Error in getHotel: " + e.getMessage());
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(Map.of("error", "Error fetching hotel", "message", e.getMessage()));
	    }
	}
	
	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<HotelModel> addHotel(
	        @RequestParam String name,
	        @RequestParam String address,
	        @RequestParam String location,
	        @RequestParam MultipartFile image) {
	    try {
	        HotelModel hotel = new HotelModel();
	        hotel.setName(name);
	        hotel.setAddress(address);
	        hotel.setLocation(location);
	        hotel.setImage(image.getBytes());
	        return ResponseEntity.ok(service.addHotel(hotel));
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().build();
	    }
	}
	
	@PutMapping(value = "/{id}", consumes = "multipart/form-data")
	public ResponseEntity<HotelModel> updateHotel(
	        @PathVariable int id,
	        @RequestParam String name,
	        @RequestParam String address,
	        @RequestParam String location,
	        @RequestParam(value = "image", required = false) MultipartFile imageFile) {
	    HotelModel existingHotel = service.getHotel(id);
	    if (existingHotel == null) {
	        return ResponseEntity.notFound().build();
	    }
	    existingHotel.setName(name);
	    existingHotel.setAddress(address);
	    existingHotel.setLocation(location);
	    if (imageFile != null && !imageFile.isEmpty()) {
	        try {
	            existingHotel.setImage(imageFile.getBytes());
	        } catch (IOException e) {
	            return ResponseEntity.badRequest().build();
	        }
	    }
	    HotelModel updated = service.updateHotel(id, existingHotel);
	    return ResponseEntity.ok(updated);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteHotel(@PathVariable int id) {
		service.deleteHotel(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/search")
    public ResponseEntity<?> searchHotels(@RequestParam String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Search keyword cannot be empty");
            }
            List<HotelModel> hotels = service.searchHotels(keyword);
            return ResponseEntity.ok(hotels);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error searching hotels: " + e.getMessage());
        }
	}
}	