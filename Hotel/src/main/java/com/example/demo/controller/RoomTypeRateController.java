package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.RoomTypeRateKey;
import com.example.demo.model.RoomTypeRateModel;
import com.example.demo.service.RoomTypeRateService;

@RestController
@RequestMapping("/rates")
public class RoomTypeRateController {

	@Autowired
	private RoomTypeRateService service;
	
	@GetMapping
	public ResponseEntity<List<RoomTypeRateModel>> getRates(){
		return ResponseEntity.ok(service.getRates());
	}
	
	@GetMapping("/{id}/{roomTypeId}/{date}")
	public ResponseEntity<RoomTypeRateModel> getRate(@PathVariable int id,@PathVariable int roomTypeId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
		RoomTypeRateModel rate = service.getRate(id, roomTypeId, date);
		return rate != null ? ResponseEntity.ok(rate) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<RoomTypeRateModel> addRate(@RequestBody RoomTypeRateModel rate){
		return ResponseEntity.ok(service.addRate(rate));
	}
	
	@PutMapping
	public ResponseEntity<RoomTypeRateModel> updateRate(@RequestBody RoomTypeRateModel rate){
		RoomTypeRateKey key = new RoomTypeRateKey(
				rate.getHotelId(),
				rate.getRoomTypeId(),
				rate.getDate()
		);
		RoomTypeRateModel updated = service.updateRate(key, rate.getRate());
		return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
	}
}