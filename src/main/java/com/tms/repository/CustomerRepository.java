package com.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tms.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

}
