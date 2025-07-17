package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.GuestModel;
import com.example.demo.repository.GuestRepository;

@Service
public class GuestService {

	@Autowired
	private GuestRepository repo;
	
	public List<GuestModel> getGuests(){
		return repo.findAll();
	}
	
	public GuestModel getGuest(int id) {
		return repo.findById(id).orElse(null);
	}

	public GuestModel addGuest(GuestModel guest) {
		return repo.save(guest);
	}
	
	public GuestModel updateGuest(int id, GuestModel guest) {
		GuestModel existing = repo.findById(id).orElse(null);
		if(existing != null) {
			return repo.save(guest);
		}
		return null;
	}
	
	public void deleteGuest(int id) {
		repo.deleteById(id);
	}
	
	public GuestModel getByEmail(String email) {
	    return repo.findByEmail(email);
	}
	
	public List<GuestModel> searchGuestsByFirstName(String keyword) {
	    return repo.findByFirstNameContainingIgnoreCase(keyword);
	}
}