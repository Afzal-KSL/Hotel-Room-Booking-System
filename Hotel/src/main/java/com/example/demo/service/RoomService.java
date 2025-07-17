package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.RoomModel;
import com.example.demo.repository.RoomRepository;

@Service
public class RoomService {

	@Autowired
	private RoomRepository repo;
	
	public List<RoomModel> getAllRooms(){
		return repo.findAll();
	}
	
	public RoomModel getRoom(int id){
		return repo.findById(id).orElse(null);
	}

	public RoomModel addRoom(RoomModel room) {
		return repo.save(room);
	}
	
	public RoomModel updateRoom(int id, RoomModel room) {
		RoomModel existing = repo.findById(id).orElse(null);
		if(existing != null) {
			return repo.save(room);
		}
		return null;
	}
	
	public void deleteRoom(int id) {
		repo.deleteById(id);
	}
	
	public List<RoomModel> getAvailableRoomsByHotel(int hotelId,int roomTypeId) {
	    return repo.findByHotel_HotelIdAndRoomTypeIdAndIsAvailableTrue(hotelId, roomTypeId);
	}
	
	public List<RoomModel> getRoomsByRoomType(int typeId) {
	    return repo.findByRoomTypeId(typeId);
	}
}