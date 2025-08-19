package com.example.demo.repository;

import com.example.demo.model.HotelModel;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<HotelModel, Integer>{
	@Query("SELECT h FROM HotelModel h WHERE " +
		       "LOWER(h.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(h.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(h.address) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<HotelModel> searchHotels(@Param("keyword") String keyword);
	
	@EntityGraph(value = "hotel-with-rooms")
    @Query("SELECT h FROM HotelModel h WHERE h.hotelId = :id")
    HotelModel findByIdWithRooms(@Param("id") int id);
}