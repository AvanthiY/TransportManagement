package com.tms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tms.model.VehicleType;

@Service
public interface VehicleTypeService {
	
	VehicleType createVehicleType(VehicleType type);

	VehicleType getById(Long vehicleTypeId);

	List<VehicleType> getAllTypes();

	void deleteVehicleType(Long vehicleTypeId);

	VehicleType updateVehicleType(Long vehicleTypeId, VehicleType vehicleType);

}
