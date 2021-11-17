package com.tms.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tms.UrlPath;
import com.tms.model.dto.VehicleDTO;
import com.tms.service.VehicleService;
import com.tms.util.ErrorMessage;

@RestController
@RequestMapping(value = UrlPath.ROOT + UrlPath.VEHICLE)
public class VehicleController {
	@Autowired
	VehicleService vehicleService;

	//Add a vehicle
	@PostMapping(UrlPath.ADD)
	@PreAuthorize("hasRole('owner') or hasRole('driver')")
	public ResponseEntity<VehicleDTO> createAppt(@RequestBody VehicleDTO vehicleDtoReq){
		return new ResponseEntity<>(vehicleService.createVehicle(vehicleDtoReq), HttpStatus.OK);
		
	}

	//Get vehicle details by vehicleId
	@PreAuthorize("hasRole('owner') or hasRole('driver')")
	@RequestMapping(value = UrlPath.GET_ID+"/{vehicleId}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable Long vehicleId ) {
		VehicleDTO vehicleDto = vehicleService.getById(vehicleId);
		if(vehicleDto != null)
			return new ResponseEntity<> (vehicleDto, HttpStatus.OK);
		else
			return new ResponseEntity<String> (ErrorMessage.NO_VEHICLES, HttpStatus.OK);
	}
	
	//Get all vehicles. Can be filtered by vehicle registration number.
	// Results are displayed by pagination(3 per page)
	@PreAuthorize("hasRole('owner') or hasRole('driver')")
	@RequestMapping(value = UrlPath.SHOW_ALL, method = RequestMethod.GET)
	public ResponseEntity<?> getAllVehicles(@RequestParam(required = false) String regNumber,
	        @RequestParam(defaultValue = UrlPath.DEFAULT_PAGE_INDEX) int page,
	        @RequestParam(defaultValue = UrlPath.DEFAULT_PAGE_SIZE) int size) {
		try {
			List<VehicleDTO> vehicleDtos = new ArrayList<>();
			vehicleDtos = vehicleService.getAllVehicles(regNumber, page, size);
			
			if(!vehicleDtos.isEmpty())
				return new ResponseEntity<>(vehicleDtos, HttpStatus.OK);
			else
				return new ResponseEntity<String>(ErrorMessage.NO_VEHICLES, HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
		      return new ResponseEntity<String>(ErrorMessage.ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Delete(soft delete) a vehicle. Only owner can delete.
	@PreAuthorize("hasRole('owner')")
	@RequestMapping(value = UrlPath.DELETE+"/{vehicleId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAppointment(@PathVariable Long vehicleId) {
		vehicleService.deleteVehicle(vehicleId);
		
		return new ResponseEntity<String>("Deleted", HttpStatus.OK);
	}
	
	//Updates details of a vehicle
	@PreAuthorize("hasRole('owner') or hasRole('driver')")
	@RequestMapping(value = UrlPath.UPDATE+"/{vehicleId}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateVehicle(@PathVariable Long vehicleId, 
					@RequestBody VehicleDTO vehicleDto) {
		
		return new ResponseEntity<VehicleDTO>
				(vehicleService.updateVehicle(vehicleId, vehicleDto), HttpStatus.OK);
	}
	
}
