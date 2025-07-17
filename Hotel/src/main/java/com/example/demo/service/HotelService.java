package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.HotelModel;
import com.example.demo.repository.HotelRepository;

@Service
public class HotelService {
	
	@Autowired
	private HotelRepository repo;
	
	public List<HotelModel> getAllHotels(){
		return repo.findAll();
	}
	
	public HotelModel getHotel(int id){
		return repo.findById(id).orElse(null);
	}
	
	public HotelModel addHotel(HotelModel hotel){
		return repo.save(hotel);
	}
	
	public HotelModel updateHotel(int id, HotelModel hotel){
		HotelModel existing = repo.findById(id).orElse(null);
		if(existing != null) {
			return repo.save(hotel);
		}
		return null;
	}
	
	public void deleteHotel(int id){
		repo.deleteById(id);
	}
	
	public List<HotelModel> searchHotels(String keyword) {
	    return repo.searchHotels(keyword);
	}
}