package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.RoomTypeInventoryModel;
import com.example.demo.repository.RoomTypeInventoryRepository;

@Service
public class RoomTypeInventoryService {

    @Autowired
    private RoomTypeInventoryRepository repo;
    
    public RoomTypeInventoryModel getCapacity(int hotelId, int roomTypeId, LocalDate date) {
        return repo.findByHotelIdAndRoomTypeIdAndDate(hotelId, roomTypeId, date).orElse(null);
    }
    
    public List<RoomTypeInventoryModel> getInventoryByHotel(int hotelId) {
        return repo.findByHotelId(hotelId);
    }
    
    public List<RoomTypeInventoryModel> getInventoryByHotelAndRoomType(int hotelId, int roomTypeId) {
        return repo.findByHotelIdAndRoomTypeId(hotelId, roomTypeId);
    }
    
    public RoomTypeInventoryModel addCapacity(RoomTypeInventoryModel inventory) {
        return repo.save(inventory);
    }
    
    public RoomTypeInventoryModel updateInventory(RoomTypeInventoryModel inventory) {
        RoomTypeInventoryModel existing = repo.findByHotelIdAndRoomTypeIdAndDate(
            inventory.getHotelId(), 
            inventory.getRoomTypeId(), 
            inventory.getDate()
        ).orElse(null);
        
        if (existing != null) {
            if (inventory.getTotalInventory() < existing.getTotalReserved()) {
                return null;
            }
            return repo.save(inventory);
        }
        return null;
    }
    
    public RoomTypeInventoryModel reserveRooms(int hotelId, int roomTypeId, LocalDate date, int count) {
        RoomTypeInventoryModel existing = repo.findByHotelIdAndRoomTypeIdAndDate(hotelId, roomTypeId, date).orElse(null);
        
        if (existing != null) {
            int availableRooms = existing.getTotalInventory() - existing.getTotalReserved();
            
            if (availableRooms >= count) {
                existing.setTotalReserved(existing.getTotalReserved() + count);
                return repo.save(existing);
            }
        }
        return null;
    }
    
    public RoomTypeInventoryModel releaseRooms(int hotelId, int roomTypeId, LocalDate date, int count) {
        RoomTypeInventoryModel existing = repo.findByHotelIdAndRoomTypeIdAndDate(hotelId, roomTypeId, date).orElse(null);
        
        if (existing != null) {
            if (existing.getTotalReserved() >= count) {
                existing.setTotalReserved(existing.getTotalReserved() - count);
                return repo.save(existing);
            }
        }
        return null;
    }
    
    public RoomTypeInventoryModel updateCapacity(int hotelId, int roomTypeId, LocalDate date, int newCapacity) {
        RoomTypeInventoryModel existing = repo.findByHotelIdAndRoomTypeIdAndDate(hotelId, roomTypeId, date).orElse(null);
        
        if (existing != null) {
            if (newCapacity >= existing.getTotalReserved()) {
                existing.setTotalInventory(newCapacity);
                return repo.save(existing);
            }
        }
        return null;
    }
}