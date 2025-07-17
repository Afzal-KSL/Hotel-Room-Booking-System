package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.ReservationModel;
import com.example.demo.repository.ReservationRepository;

@Service
public class ReservationService {

	@Autowired
	private ReservationRepository repo;
	
	public List<ReservationModel> getReservations(){
		return repo.findAll();
	}
	
	public ReservationModel getReservation(int id) {
		return repo.findById(id).orElse(null);
	}
	
	public ReservationModel addReservation(ReservationModel reservation) {
	    int hotelId = reservation.getHotel().getHotelId();
	    int roomId = reservation.getRoom().getRoomId();
	    LocalDate start = reservation.getStartDate();
	    LocalDate end = reservation.getEndDate();

	    boolean hasConflict = hasOverlappingReservation(hotelId, roomId, start, end);

	    if (hasConflict) {
	        return null;
	    }
		return repo.save(reservation);
	}
	
	public ReservationModel updateReservation(int id, ReservationModel reservation) {
		ReservationModel existing = repo.findById(id).orElse(null);
		if(existing != null) {
			return repo.save(reservation);
		}
		return null;
	}
	
	public void deleteReservation(int id) {
		repo.deleteById(id);
	}
	
	public boolean hasOverlappingReservation(int hotelId, int roomId, LocalDate start, LocalDate end) {
	    return !repo.findOverlappingReservations(hotelId, roomId, start, end).isEmpty();
	}
	
	public List<ReservationModel> getReservationsByGuest(int guestId) {
	    return repo.findByGuest_GuestId(guestId);
	}

	public List<ReservationModel> getReservationsByHotel(int hotelId) {
	    return repo.findByHotel_HotelId(hotelId);
	}
}