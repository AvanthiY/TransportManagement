package com.tms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE vehicle SET is_deleted=true WHERE id=?")
@Where(clause = "is_deleted = false")
public class Vehicle {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	private String regNumber;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

	@ManyToOne
    @JoinColumn(name = "vehicle_type_id")
    private VehicleType vehicleType;
    
    private boolean isActive = true;
	
    private boolean isDeleted = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRegNumber() {
		return regNumber;
	}

	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
    public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


}
