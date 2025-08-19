package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.RoomModel;
import com.example.demo.model.RoomTypeInventoryModel;
import com.example.demo.repository.RoomRepository;

@Service
public class RoomService {

	@Autowired
	private RoomRepository repo;
	
	@Autowired
    private RoomTypeInventoryService inventoryService;
    
    @Autowired
    private ReservationService reservationService;
    
    public boolean checkRoomAvailabilityForDates(int roomId, LocalDate checkIn, LocalDate checkOut) {
        try {
            RoomModel room = repo.findById(roomId).orElse(null);
            if (room == null || !room.isAvailable()) {
                return false;
            }
            
            boolean hasConflict = reservationService.hasOverlappingReservation(
                room.getHotel().getHotelId(), 
                roomId, 
                checkIn, 
                checkOut
            );
            
            if (hasConflict) {
                return false;
            }
            
            LocalDate currentDate = checkIn;
            while (!currentDate.isAfter(checkOut.minusDays(1))) {
                RoomTypeInventoryModel inventory = inventoryService.getCapacity(
                    room.getHotel().getHotelId(),
                    room.getRoomTypeId(),
                    currentDate
                );
                
                if (inventory == null || inventory.getTotalInventory() <= inventory.getTotalReserved()) {
                    return false;
                }
                
                currentDate = currentDate.plusDays(1);
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Error checking room availability: " + e.getMessage());
            return false;
        }
    }
	
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