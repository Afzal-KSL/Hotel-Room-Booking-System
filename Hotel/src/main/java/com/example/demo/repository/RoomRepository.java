package com.example.demo.repository;

import com.example.demo.model.RoomModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<RoomModel, Integer>{
	List<RoomModel> findByHotel_HotelIdAndRoomTypeIdAndIsAvailableTrue(int hotelId, int roomTypeId);
	List<RoomModel> findByRoomTypeId(int typeId);
}