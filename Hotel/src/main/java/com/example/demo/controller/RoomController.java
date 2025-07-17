package com.example.demo.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.model.HotelModel;
import com.example.demo.model.RoomModel;
import com.example.demo.service.HotelService;
import com.example.demo.service.RoomService;

@RestController
@RequestMapping("/rooms")
public class RoomController {

	@Autowired
	private RoomService service;
	
	@Autowired
	private HotelService hotelService;
	
	@GetMapping
	public ResponseEntity<List<RoomModel>> getAllRooms(){
		return ResponseEntity.ok(service.getAllRooms());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<RoomModel> getRoom(@PathVariable int id){
		RoomModel room = service.getRoom(id);
		return room != null ? ResponseEntity.ok(room) : ResponseEntity.notFound().build();
	}
	
	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<RoomModel> addRoom(
			@RequestParam int roomTypeId,
			@RequestParam int floor,
			@RequestParam int number,
	        @RequestParam String name,
	        @RequestParam boolean isAvailable,
	        @RequestParam int hotelId,
	        @RequestParam MultipartFile image
	) {
	    try {
	        RoomModel room = new RoomModel();
	        room.setRoomTypeId(roomTypeId);
	        room.setFloor(floor);
	        room.setNumber(number);
	        room.setName(name);
	        room.setAvailable(isAvailable);
	        room.setImage(image.getBytes());

	        HotelModel hotel = hotelService.getHotel(hotelId);
	        if (hotel == null) {
	            return ResponseEntity.badRequest().build();
	        }
	        room.setHotel(hotel);
	        return ResponseEntity.ok(service.addRoom(room));
	    } catch (IOException e) {
	        return ResponseEntity.internalServerError().build();
	    }
	}
	
	@PutMapping(value = "/{id}", consumes = "multipart/form-data")
	public ResponseEntity<RoomModel> updateRoom(
	        @PathVariable int id,
	        @RequestParam int roomTypeId,
	        @RequestParam int floor,
	        @RequestParam int number,
	        @RequestParam String name,
	        @RequestParam boolean isAvailable,
	        @RequestParam int hotelId,
	        @RequestParam(required = false) MultipartFile image
	) {
	    try {
	        RoomModel room = service.getRoom(id);
	        if (room == null) {
	            return ResponseEntity.notFound().build();
	        }

	        room.setRoomTypeId(roomTypeId);
	        room.setFloor(floor);
	        room.setNumber(number);
	        room.setName(name);
	        room.setAvailable(isAvailable);

	        if (image != null && !image.isEmpty()) {
	            room.setImage(image.getBytes());
	        }

	        HotelModel hotel = hotelService.getHotel(hotelId);
	        if (hotel == null) {
	            return ResponseEntity.badRequest().build();
	        }
	        room.setHotel(hotel);

	        RoomModel updated = service.updateRoom(id, room);
	        return ResponseEntity.ok(updated);

	    } catch (IOException e) {
	        return ResponseEntity.internalServerError().build();
	    }
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRoom(@PathVariable int id){
		service.deleteRoom(id);
		return ResponseEntity.noContent().build();
	}
}