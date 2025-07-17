package com.example.demo.repository;

import com.example.demo.model.RoomTypeRateKey;
import com.example.demo.model.RoomTypeRateModel;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRateRepository extends JpaRepository<RoomTypeRateModel, RoomTypeRateKey>{
	Optional<RoomTypeRateModel> findByHotelIdAndRoomTypeIdAndDate(int hotelId,int roomTypeId, LocalDate date);	
}