package com.tms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tms.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle,Long>{
	
	Page<Vehicle> findByRegNumberContaining(String regNumber, Pageable paging);

}
