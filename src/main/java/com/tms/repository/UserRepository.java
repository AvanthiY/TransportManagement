package com.tms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tms.model.User;


public interface UserRepository extends JpaRepository<User, Long>{
	User findByUsername(String username);
	
	Page<User> findByUsernameContaining(String username, Pageable paging);
}
