package com.example.demo.repository;

import com.example.demo.model.RoomTypeInventoryKey;
import com.example.demo.model.RoomTypeInventoryModel;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeInventoryRepository extends JpaRepository<RoomTypeInventoryModel, RoomTypeInventoryKey>{
    Optional<RoomTypeInventoryModel> findByHotelIdAndRoomTypeIdAndDate(int hotelId, int roomTypeId, LocalDate date);
    List<RoomTypeInventoryModel> findByHotelId(int hotelId);
    List<RoomTypeInventoryModel> findByHotelIdAndRoomTypeId(int hotelId, int roomTypeId);
}