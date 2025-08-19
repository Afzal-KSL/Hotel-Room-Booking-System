package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.GuestModel;
import com.example.demo.service.GuestService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/guests")
public class GuestController {

	@Autowired
	private GuestService service;
	
	@GetMapping
	public ResponseEntity<List<GuestModel>> getGuests(){
		return ResponseEntity.ok(service.getGuests());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<GuestModel> getGuest(@PathVariable int id){
		GuestModel guest = service.getGuest(id);
		return guest != null ? ResponseEntity.ok(guest) : ResponseEntity.notFound().build();
	}
	
	@GetMapping("/by-email")
	public ResponseEntity<GuestModel> getGuestByEmail(@RequestParam("email") String email) {
	    GuestModel guest = service.getByEmail(email);
	    return guest != null ? ResponseEntity.ok(guest) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<GuestModel> addGuest(@RequestBody @Valid GuestModel guest){
		return ResponseEntity.ok(service.addGuest(guest));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<GuestModel> updateGuest(@PathVariable int id, @RequestBody GuestModel guest){
		GuestModel updated = service.updateGuest(id, guest);
		return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteGuest(@PathVariable int id){
		service.deleteGuest(id);
		return ResponseEntity.noContent().build();
	}
}