package com.tms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tms.model.Vehicle;
import com.tms.model.VehicleType;
import com.tms.model.Owner;
import com.tms.model.Customer;
import com.tms.model.Driver;
import com.tms.model.dto.VehicleDTO;
import com.tms.repository.CustomerRepository;
import com.tms.repository.DriverRepository;
import com.tms.repository.OwnerRepository;
import com.tms.repository.VehicleRepository;
import com.tms.repository.VehicleTypeRepository;
import com.tms.service.VehicleService;

@Service
public class VehicleServiceImpl implements VehicleService{
	
	@Autowired
	VehicleRepository vehicleRepo;
	
	@Autowired
	OwnerRepository ownerRepo;
	
	@Autowired
	CustomerRepository customerRepo;
	
	@Autowired
	DriverRepository driverRepo;
	
	@Autowired
	VehicleTypeRepository vehicleTypeRepo;

	@Override
	public VehicleDTO createVehicle(VehicleDTO vehicleDto) {
		
		Vehicle vehicle = new Vehicle();
		VehicleDTO vehicleDtoResponse = null; 
		
		if(vehicleDto != null) {
			Owner owner = ownerRepo.getById(vehicleDto.getOwnerId());		
			Driver driver = null;
			Customer customer = null;
			VehicleType vehicleType = null;
			
			if(vehicleDto.getDriverId() != null)
				 driver = driverRepo.getById(vehicleDto.getDriverId());
			
			if(vehicleDto.getCustomerId() != null)
				customer = customerRepo.getById(vehicleDto.getCustomerId());
			
			if(vehicleDto.getVehicleType() != null)
				vehicleType = vehicleTypeRepo.getById(vehicleDto.getVehicleType());
			
			if(owner != null) {
				vehicle.setVehicleType(vehicleType);
				vehicle.setOwner(owner);
				vehicle.setDriver(driver);
				vehicle.setCustomer(customer);
				vehicle.setRegNumber(vehicleDto.getRegNumber());
				
				Vehicle vehicleRes = vehicleRepo.save(vehicle);
				
				vehicleDtoResponse = updateDto(vehicleRes);
			}
		}	
		return vehicleDtoResponse;
	}
	
	private VehicleDTO updateDto (Vehicle vehicle) {
		VehicleDTO vehicleDto = new VehicleDTO();
		
		vehicleDto.setRegNumber(vehicle.getRegNumber());
		if(vehicle.getOwner() != null)
			vehicleDto.setOwnerId(vehicle.getOwner().getId());
		
		if(vehicle.getVehicleType() != null)
			vehicleDto.setVehicleType(vehicle.getVehicleType().getId());
		
		if(vehicle.getDriver() != null)
			vehicleDto.setDriverId(vehicle.getDriver().getId());
		
		if(vehicle.getCustomer() != null)
			vehicleDto.setCustomerId(vehicle.getCustomer().getId());
		
		return vehicleDto;
	}

	@Override
	public VehicleDTO getById(Long vehicleId) {
		Optional<Vehicle> vehicle = vehicleRepo.findById(vehicleId);

		if (vehicle.isPresent()) {
			 VehicleDTO vehicleDto = updateDto(vehicle.get());
	    	 
			return vehicleDto;
		}
		return null;
	}

	@Override
	public List<VehicleDTO> getAllVehicles(String regNumber, int page, int size) {

    	List<Vehicle> vehicles = new ArrayList<>();
    	List<VehicleDTO> vehicleDtos = new ArrayList<>();
    	
    	Pageable paging = PageRequest.of(page, size);
      
    	Page<Vehicle> pageVehicles;
    	
    	if (regNumber == null)
    		pageVehicles = vehicleRepo.findAll(paging);
    	else
    		pageVehicles = vehicleRepo.findByRegNumberContaining(regNumber, paging);

    	if(pageVehicles.hasContent()) {
    		vehicles = pageVehicles.getContent();
	
    		vehicles.stream().forEach(vehicle-> {
    			VehicleDTO vehicleDto = updateDto(vehicle);
		    	  
    			vehicleDtos.add(vehicleDto);
		      });
    	}
    	
    	return vehicleDtos;
	}

	@Override
	public void deleteVehicle(Long vehicleId) {
		vehicleRepo.deleteById(vehicleId);
	}

	@Override
	public VehicleDTO updateVehicle(Long vehicleId, VehicleDTO vehicleDto) {
		Vehicle vehicle = vehicleRepo.getById(vehicleId);
		
		if(vehicle != null) {
			vehicle.setRegNumber(vehicleDto.getRegNumber());
			
			if(vehicle.getVehicleType().getId() != vehicleDto.getVehicleType())
			{
				vehicle.setVehicleType(vehicleTypeRepo.findById(vehicleDto.getVehicleType()).get());
			}
			if(vehicleDto.getOwnerId() != null)
			{
				vehicle.setOwner(ownerRepo.findById(vehicleDto.getOwnerId()).get());
			}
			if(vehicleDto.getDriverId() != null)
			{
				vehicle.setDriver(driverRepo.findById(vehicleDto.getDriverId()).get());
			}
			if(vehicleDto.getCustomerId() != null)
			{
				vehicle.setCustomer(customerRepo.findById(vehicleDto.getCustomerId()).get());
			}
		}
		
		Vehicle vehicleRes = vehicleRepo.save(vehicle);
		
		return updateDto(vehicleRes);
	}

}
