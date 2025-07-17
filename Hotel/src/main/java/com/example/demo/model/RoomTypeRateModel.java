package com.example.demo.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(RoomTypeRateKey.class)
public class RoomTypeRateModel {
	
	@Id
	private int hotelId;
	@Id
	private int roomTypeId;
	@Id
	private LocalDate date;
	
	private int rate;
}
