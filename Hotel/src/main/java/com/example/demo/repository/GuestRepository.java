package com.example.demo.repository;

import com.example.demo.model.GuestModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<GuestModel, Integer>{
	GuestModel findByEmail(String email);
	List<GuestModel> findByFirstNameContainingIgnoreCase(String firstName);	
}