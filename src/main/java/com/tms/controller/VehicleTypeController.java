package com.tms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tms.UrlPath;
import com.tms.model.VehicleType;
import com.tms.service.VehicleTypeService;
import com.tms.util.ErrorMessage;

@RestController
@RequestMapping(value = UrlPath.ROOT + UrlPath.VEHICLE_TYPE)
public class VehicleTypeController {

	
	@Autowired
	private VehicleTypeService vehicleTypeService;
	
	@PostMapping(UrlPath.ADD)
	@PreAuthorize("hasRole('owner')")
	public ResponseEntity<VehicleType> createVehicleType(@RequestBody VehicleType vehicleType){
		return new ResponseEntity<>(vehicleTypeService.createVehicleType(vehicleType), HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasRole('owner')")
	@RequestMapping(value = UrlPath.GET_ID+"/{vehicleTypeId}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable Long vehicleTypeId ) {
		VehicleType vehicleType = vehicleTypeService.getById(vehicleTypeId);

		if(vehicleType != null)
			return new ResponseEntity<> (vehicleType, HttpStatus.OK);
		else
			return new ResponseEntity<String> (ErrorMessage.NO_VEHICLES, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('owner')")
	@RequestMapping(value = UrlPath.SHOW_ALL, method = RequestMethod.GET)
	public ResponseEntity<?> getAllVehicleTypes() {
		List<VehicleType> typesList = vehicleTypeService.getAllTypes();
		
		if(typesList != null) 
			return new ResponseEntity<>(typesList, HttpStatus.OK);
		else
			return new ResponseEntity<String> (ErrorMessage.NO_VEHICLES, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('owner')")
	@RequestMapping(value = UrlPath.DELETE+"/{vehicleTypeId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteApptType(@PathVariable Long vehicleTypeId) {
		vehicleTypeService.deleteVehicleType(vehicleTypeId);
		
		return new ResponseEntity<String>("Deleted", HttpStatus.OK);
	}

	@PreAuthorize("hasRole('owner')")
	@RequestMapping(value = UrlPath.UPDATE+"/{vehicleTypeId}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateApptType(@PathVariable Long vehicleTypeId, 
			@RequestBody VehicleType vehicleType) {
		
		return new ResponseEntity<VehicleType> 
					(vehicleTypeService.updateVehicleType(vehicleTypeId, vehicleType), HttpStatus.OK);
		
	}


}
