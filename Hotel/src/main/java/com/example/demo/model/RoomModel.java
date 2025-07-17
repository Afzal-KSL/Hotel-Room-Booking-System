package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RoomModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int roomId;
	
	@Lob
	private byte[] image;
	@NotNull(message = "Room type is required")
	private int roomTypeId;
	@NotNull(message = "Floor is required")
	private int floor;
	@NotNull(message = "Room number is required")
	private int number;
	
	@NotNull(message = "Hotel is required")
	@ManyToOne
	@JoinColumn(name = "hotel_id")
	private HotelModel hotel;
	
	@NotBlank(message = "Room name is required")
	private String name;
	@NotNull
	private boolean isAvailable;
}
