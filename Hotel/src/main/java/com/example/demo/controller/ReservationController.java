package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.ReservationRequestDTO;
import com.example.demo.model.GuestModel;
import com.example.demo.model.HotelModel;
import com.example.demo.model.ReservationModel;
import com.example.demo.model.RoomModel;
import com.example.demo.service.GuestService;
import com.example.demo.service.HotelService;
import com.example.demo.service.ReservationService;
import com.example.demo.service.RoomService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService service;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private GuestService guestService;

    @Autowired
    private RoomService roomService;

    @GetMapping
    public ResponseEntity<List<ReservationModel>> getReservations(){
        return ResponseEntity.ok(service.getReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationModel> getReservation(@PathVariable int id){
        ReservationModel reservation = service.getReservation(id);
        return reservation != null ? ResponseEntity.ok(reservation) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> addReservation(@Valid @RequestBody ReservationRequestDTO dto) {
        // Validate that all required entities exist
        HotelModel hotel = hotelService.getHotel(dto.getHotelId());
        if (hotel == null) {
            return ResponseEntity.badRequest().body("Hotel with ID " + dto.getHotelId() + " not found");
        }

        GuestModel guest = guestService.getGuest(dto.getGuestId());
        if (guest == null) {
            return ResponseEntity.badRequest().body("Guest with ID " + dto.getGuestId() + " not found");
        }

        RoomModel room = roomService.getRoom(dto.getRoomId());
        if (room == null) {
            return ResponseEntity.badRequest().body("Room with ID " + dto.getRoomId() + " not found");
        }

        // Validate that the room belongs to the hotel
        if (room.getHotel().getHotelId() != hotel.getHotelId()) {
            return ResponseEntity.badRequest().body("Room " + dto.getRoomId() + " does not belong to hotel " + dto.getHotelId());
        }

        // Validate dates
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            return ResponseEntity.badRequest().body("Start date must be before end date");
        }

        ReservationModel reservation = new ReservationModel();
        reservation.setHotel(hotel);
        reservation.setGuest(guest);
        reservation.setRoom(room);
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());

        ReservationModel saved = service.addReservation(reservation);
        if (saved == null) {
            return ResponseEntity.badRequest().body("Room is already booked for the selected dates");
        }
        
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable int id, @Valid @RequestBody ReservationRequestDTO dto){
       
        ReservationModel existing = service.getReservation(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        
        HotelModel hotel = hotelService.getHotel(dto.getHotelId());
        if (hotel == null) {
            return ResponseEntity.badRequest().body("Hotel with ID " + dto.getHotelId() + " not found");
        }

        GuestModel guest = guestService.getGuest(dto.getGuestId());
        if (guest == null) {
            return ResponseEntity.badRequest().body("Guest with ID " + dto.getGuestId() + " not found");
        }

        RoomModel room = roomService.getRoom(dto.getRoomId());
        if (room == null) {
            return ResponseEntity.badRequest().body("Room with ID " + dto.getRoomId() + " not found");
        }

        if (room.getHotel().getHotelId() != hotel.getHotelId()) {
            return ResponseEntity.badRequest().body("Room " + dto.getRoomId() + " does not belong to hotel " + dto.getHotelId());
        }

        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            return ResponseEntity.badRequest().body("Start date must be before end date");
        }

        ReservationModel reservation = new ReservationModel();
        reservation.setHotel(hotel);
        reservation.setGuest(guest);
        reservation.setRoom(room);
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());

        ReservationModel updated = service.updateReservation(id, reservation);
        if (updated == null) {
            return ResponseEntity.badRequest().body("Room is already booked for the selected dates");
        }
        
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable int id){
        service.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}