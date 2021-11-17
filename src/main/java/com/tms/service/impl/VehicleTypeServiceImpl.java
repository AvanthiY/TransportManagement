package com.tms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tms.model.VehicleType;
import com.tms.repository.VehicleTypeRepository;
import com.tms.service.VehicleTypeService;

@Service
public class VehicleTypeServiceImpl implements VehicleTypeService{
	
	@Autowired
	VehicleTypeRepository vehicleTypeRepo;
	
	@Override
	public VehicleType createVehicleType(VehicleType type) {
		VehicleType vehicleType = vehicleTypeRepo.save(type);
		return vehicleType;
	}

	@Override
	public VehicleType getById(Long vehicleTypeId) {
		Optional<VehicleType> vType = null;
		
		if (vehicleTypeId != null) {
			vType = vehicleTypeRepo.findById(vehicleTypeId);
		}
		
		return vType.get();
	}

	@Override
	public List<VehicleType> getAllTypes() {
		List<VehicleType> vTypeList = vehicleTypeRepo.findAll();
		return vTypeList;
	}

	@Override
	public void deleteVehicleType(Long vehicleTypeId) {
		vehicleTypeRepo.deleteById(vehicleTypeId);
	}
	
	public VehicleType updateVehicleType(Long vehicleTypeId, VehicleType appType) {
		VehicleType vehicleTypeDb = vehicleTypeRepo.getById(vehicleTypeId);
		
		if(vehicleTypeDb != null) {
			vehicleTypeDb.setName(appType.getName());
			vehicleTypeDb.setDeleted(appType.isDeleted());
		}
		
		return vehicleTypeRepo.save(vehicleTypeDb); 
	}



}
