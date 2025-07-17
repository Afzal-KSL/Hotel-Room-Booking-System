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
import com.example.demo.service.HotelService;

@RestController
@RequestMapping("/hotels")
public class HotelController {

	@Autowired
	private HotelService service;
	
	@GetMapping
	public ResponseEntity<List<HotelModel>> getAllHotels(){
		return ResponseEntity.ok(service.getAllHotels());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<HotelModel> getHotel(@PathVariable int id){
		HotelModel hotel = service.getHotel(id);
		return hotel != null ? ResponseEntity.ok(hotel) : ResponseEntity.notFound().build();
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
	public ResponseEntity<List<HotelModel>> searchHotels(@RequestParam String keyword) {
	    return ResponseEntity.ok(service.searchHotels(keyword));
	}
}