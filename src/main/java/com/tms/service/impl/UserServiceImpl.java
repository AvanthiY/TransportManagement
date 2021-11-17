package com.tms.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.tms.repository.CustomerRepository;
import com.tms.repository.DriverRepository;
import com.tms.repository.OwnerRepository;
import com.tms.repository.RolesRepository;
import com.tms.repository.UserRepository;
import com.tms.service.UserService;
import com.tms.model.dto.UserDTO;
import com.tms.model.Customer;
import com.tms.model.Driver;
import com.tms.model.Owner;
import com.tms.model.Roles;
import com.tms.model.User;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RolesRepository rolesRepo;
	
	@Autowired
	OwnerRepository ownerRepo;
	
	@Autowired
	DriverRepository driverRepo;
	
	@Autowired
	CustomerRepository customerRepo;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Autowired
	private ModelMapper modelMapper;


	@Override
	public UserDTO addUser(String username, String passwd, String mobileNo, List<Integer> roles) {
		User user = new User();
		user.setPassword(bcryptEncoder.encode(passwd));
		user.setUsername(username);
		user.setMobileNo(mobileNo);
		
		Set<Roles> userRoles = new HashSet<>();
		for (Integer id : roles) { 
			Optional<Roles> role = rolesRepo.findById(id);
			if (role.isPresent()) {
				userRoles.add(role.get());
			}
		}
		 
		if (!CollectionUtils.isEmpty(userRoles)) {
			user.setRoles(userRoles);
		}

		user = userRepo.save(user);

		if (!CollectionUtils.isEmpty(userRoles)) {
			checkRolesAndAdd(user);
		}
		
		UserDTO userDTO = modelMapper.map(user, UserDTO.class);

		return userDTO;
	}


	private void checkRolesAndAdd(User user) {
		Set<Roles> userRoles = user.getRoles();
		
		for(Roles role : userRoles) {
			String roleName = role.getName();
			if(roleName.equalsIgnoreCase("owner")) {
				//create an entry in the owner table
				Owner owner = new Owner();
				owner.setName(user.getUsername());
				owner.setUser(user);
				
				ownerRepo.save(owner);
			}else if (roleName.equalsIgnoreCase("customer")) {
				//create an entry in the customer table
				Customer customer = new Customer();
				customer.setName(user.getUsername());
				customer.setUser(user);
				
				customerRepo.save(customer);
				
			}else if(roleName.equalsIgnoreCase("driver")) {
				//create an entry in the driver table
				Driver driver = new Driver();
				
				driver.setName(user.getUsername());
				driver.setUser(user);
				
				driverRepo.save(driver);
			}
			
		}
		
	}


	public UserDTO getById(Long userId) {
		Optional<User> user = userRepo.findById(userId);

		if (user.isPresent()) {
			UserDTO userDTO = modelMapper.map(user.get(), UserDTO.class);

			return userDTO;
		}

		return null;

	}


	public List<UserDTO> getAllUsers(String username, int page, int size) {

		modelMapper.getConfiguration()
        .setMatchingStrategy(MatchingStrategies.LOOSE);
		
	    	List<User> users = new ArrayList<>();
	    	List<UserDTO> userDtos = new ArrayList<>();
	    	
	    	Pageable paging = PageRequest.of(page, size);
	      
	    	Page<User> pageUsers;
	    	
	    	if (username == null)
	    		pageUsers = userRepo.findAll(paging);
	    	else
	    		pageUsers = userRepo.findByUsernameContaining(username, paging);

	    	if(pageUsers.hasContent()) {
		    	users = pageUsers.getContent();
		
		    	users.stream().forEach(p-> {
			    	  UserDTO user = modelMapper.map(p, UserDTO.class);
					  userDtos.add(user);
			      });
	    	}	      

	return userDtos;
	}


	public void deleteUser(Long userId) {
		Optional<User> user = userRepo.findById(userId);

		//Remove all the roles attached to this user
		if (user.isPresent()) {
			Set<Roles> roles = (user.get().getRoles());
			if (!roles.isEmpty()){
				roles.removeAll(roles);
				 userRepo.save(user.get());
			}
			
			userRepo.deleteById(userId);
		}
		
	}


	@Override
	public UserDTO updateUser(Long userId, UserDTO userDto) {
		User user = userRepo.getById(userId);
		
		if(user != null) {
			
			user.setUsername(userDto.getUsername());
			user.setMobileNo(userDto.getMobileNo());
			user.setRoles(userDto.getRoles());
			
			User userNew = userRepo.save(user);
		
			return modelMapper.map(userNew, UserDTO.class);
		}

		return null;
	}

}
