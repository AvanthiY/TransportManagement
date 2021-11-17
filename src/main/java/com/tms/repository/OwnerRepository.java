package com.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tms.model.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Long>{

}
