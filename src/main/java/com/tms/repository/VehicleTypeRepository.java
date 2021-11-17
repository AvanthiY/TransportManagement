package com.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tms.model.VehicleType;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long>{
	
}
