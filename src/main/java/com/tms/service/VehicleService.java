package com.tms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tms.model.dto.VehicleDTO;

@Service
public interface VehicleService {

	VehicleDTO createVehicle(VehicleDTO vehicleDto);

	List<VehicleDTO> getAllVehicles(String regNumber, int page, int size);

	VehicleDTO getById(Long vehicleId);

	void deleteVehicle(Long vehicleId);

	VehicleDTO updateVehicle(Long vehicleId, VehicleDTO vehicleDto);

}
