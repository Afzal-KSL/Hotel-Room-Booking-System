package com.example.demo.model;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeInventoryKey implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int hotelId;
	private int roomTypeId;
	private LocalDate date;
}
