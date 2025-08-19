package com.example.demo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedEntityGraph(name = "hotel-with-rooms", attributeNodes = @NamedAttributeNode("rooms"))
public class HotelModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int hotelId;

	@Lob
	@JsonProperty("image")
	private byte[] image;
	@NotBlank(message = "Hotel name is required")
	private String name;
	@NotBlank(message = "Address is required")
	private String address;
	@NotBlank(message = "Location is required")
	private String location;
	
	@OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<RoomModel> rooms;
}