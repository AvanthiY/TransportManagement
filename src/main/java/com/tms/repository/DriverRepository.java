package com.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tms.model.Driver;

public interface DriverRepository extends JpaRepository<Driver, Long>{

}
