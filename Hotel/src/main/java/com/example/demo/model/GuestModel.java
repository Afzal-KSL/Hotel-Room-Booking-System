package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class GuestModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int guestId;
	
	@NotBlank(message = "First Name is required")
	private String firstName;
	@NotBlank(message = "Last Name is required")
	private String lastName;
	
	@Email(message = "Email should be valid")
	@NotBlank(message = "Email is required")
	private String email;
	
	@OneToMany(mappedBy = "guest")
	@JsonIgnore
	private List<ReservationModel> reservations;
}
