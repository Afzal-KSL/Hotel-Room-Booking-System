package com.example.demo.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReservationModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int reservationId;
	
	@ManyToOne
	@JoinColumn(name = "hotel_id")
	private HotelModel hotel;
	
	private LocalDate startDate;
	private LocalDate endDate;
	
	@ManyToOne
	@JoinColumn(name = "guest_id")
	private GuestModel guest;
	
	@ManyToOne
	@JoinColumn(name = "room_id")
	private RoomModel room;
}
