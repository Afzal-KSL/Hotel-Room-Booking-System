package com.example.demo.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.HotelModel;
import com.example.demo.model.RoomModel;
import com.example.demo.model.RoomTypeInventoryModel;
import com.example.demo.repository.HotelRepository;


@Service
@Transactional
public class HotelService {
	
	@Autowired
	private HotelRepository repo;
	
	@Autowired
	private RoomTypeInventoryService inventoryService;
    
	@Transactional(readOnly = true)
    public HotelModel getHotelWithRoomsAndCheckInventory(int id, LocalDate checkDate) {
        try {
            HotelModel hotel = repo.findByIdWithRooms(id);
            if (hotel != null && hotel.getRooms() != null) {
                // Force initialization
                Hibernate.initialize(hotel.getRooms());
                
                // Update room availability based on inventory
                for (RoomModel room : hotel.getRooms()) {
                    boolean hasInventory = checkRoomInventoryAvailability(
                        hotel.getHotelId(), 
                        room.getRoomTypeId(), 
                        checkDate
                    );
                    
                    // Override the isAvailable field temporarily for this response
                    room.setAvailable(room.isAvailable() && hasInventory);
                    
                    System.out.println("Room " + room.getName() + 
                        " (RoomType: " + room.getRoomTypeId() + ")" +
                        " - DB Available: " + room.isAvailable() + 
                        " - Inventory Available: " + hasInventory + 
                        " - Final: " + room.isAvailable());
                }
            }
            return hotel;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching hotel with ID: " + id, e);
        }
    }
    
    // Your existing method logic
	private boolean checkRoomInventoryAvailability(int hotelId, int roomTypeId, LocalDate date) {
        try {
            RoomTypeInventoryModel inventory = inventoryService.getCapacity(hotelId, roomTypeId, date);
            
            if (inventory == null) {
                System.out.println("❌ No inventory found for hotel " + hotelId + 
                    ", roomType " + roomTypeId + ", date " + date);
                return false;
            }
            
            boolean available = inventory.getTotalInventory() > inventory.getTotalReserved();
            System.out.println("✅ Inventory check - Hotel: " + hotelId + 
                ", RoomType: " + roomTypeId + 
                ", Total: " + inventory.getTotalInventory() + 
                ", Reserved: " + inventory.getTotalReserved() + 
                ", Available: " + available);
            
            return available;
        } catch (Exception e) {
            System.err.println("Error checking inventory: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
	
	public List<HotelModel> getAllHotels(){
        try {
            return repo.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching hotels from database", e);
        }
    }
	
	public HotelModel getHotelWithRooms(int id){
        try {
            return repo.findByIdWithRooms(id);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching hotel with ID: " + id, e);
        }
    }
	
	@Transactional(readOnly = true)
    public HotelModel getHotel(int id) {
        try {
            HotelModel hotel = repo.findByIdWithRooms(id);
            if (hotel != null) {
                Hibernate.initialize(hotel.getRooms());
            }
            return hotel;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching hotel with ID: " + id, e);
        }
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
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return Collections.emptyList();
            }
            return repo.searchHotels(keyword.trim());
        } catch (Exception e) {
            throw new RuntimeException("Error searching hotels with keyword: " + keyword, e);
        }
    }
}