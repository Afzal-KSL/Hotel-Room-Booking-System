package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.RoomTypeRateKey;
import com.example.demo.model.RoomTypeRateModel;
import com.example.demo.repository.RoomTypeRateRepository;

@Service
public class RoomTypeRateService {

	@Autowired
	private RoomTypeRateRepository repo;
	
	public List<RoomTypeRateModel> getRates(){
		return repo.findAll();
	}
	
	public RoomTypeRateModel getRate(int hotelId, int roomTypeId, LocalDate date) {
	    return repo.findByHotelIdAndRoomTypeIdAndDate(hotelId, roomTypeId, date).orElse(null);
	}
	
	public RoomTypeRateModel getRate(RoomTypeRateKey rateKey) {
		return repo.findById(rateKey).orElse(null);
	}
	
	public RoomTypeRateModel addRate(RoomTypeRateModel rate) {
		return repo.save(rate);
	}
	
	public RoomTypeRateModel updateRate(RoomTypeRateKey rate, int value) {
		RoomTypeRateModel existing = repo.findById(rate).orElse(null);
		if(existing != null) {
			existing.setRate(value);
			return repo.save(existing);
		}
		return null;
	}
}