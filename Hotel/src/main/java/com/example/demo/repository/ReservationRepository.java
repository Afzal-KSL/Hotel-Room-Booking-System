package com.example.demo.repository;

import com.example.demo.model.ReservationModel;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationModel, Integer>{
	List<ReservationModel> findByGuest_GuestId(int guestId);
	List<ReservationModel> findByHotel_HotelId(int hotelId);
	
	@Query("SELECT r FROM ReservationModel r WHERE r.hotel.hotelId = :hotelId AND r.room.roomId = :roomId AND " +
		       "(:startDate < r.endDate AND :endDate > r.startDate)")
	List<ReservationModel> findOverlappingReservations(int hotelId, int roomId, LocalDate startDate, LocalDate endDate);
}