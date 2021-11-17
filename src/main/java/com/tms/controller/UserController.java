package com.tms.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tms.model.dto.UserDTO;
import com.tms.service.UserService;
import com.tms.UrlPath;
import com.tms.model.AuthenticationRequest;
import com.tms.model.AuthenticationResponse;
import com.tms.util.ErrorMessage;
import com.tms.util.jwt.JwtUtil;

@RestController
@RequestMapping(value = UrlPath.ROOT + UrlPath.OWNER)
public class UserController {
	
	@Autowired
	private AuthenticationManager authMgr;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtil jwtUtil;

	@RequestMapping(value = UrlPath.LOGIN, method= RequestMethod.POST)
	public ResponseEntity<?> userAuthentication(@RequestBody AuthenticationRequest authReq) throws Exception{
		String token;
		
		try {
			final Authentication authentication = authMgr
					.authenticate(new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);

			token = jwtUtil.generateToken(authentication);
		} catch (DisabledException e) {
			return new ResponseEntity<String>(ErrorMessage.USER_DISABLED, HttpStatus.FORBIDDEN);
		} catch (BadCredentialsException e) {
			return new ResponseEntity<String>(ErrorMessage.BAD_CREDENTIALS, HttpStatus.FORBIDDEN);
		}
		
		return new ResponseEntity<AuthenticationResponse>
				(new AuthenticationResponse(authReq.getUsername(), token), HttpStatus.OK);
	
	}
	
	@RequestMapping(value = UrlPath.ADD, method = RequestMethod.POST)
	public ResponseEntity<UserDTO> signup(@RequestBody AuthenticationRequest authRequest) {
		return ResponseEntity.ok(userService.addUser(authRequest.getUsername(),
				authRequest.getPassword(), authRequest.getMobileNo(), authRequest.getRoles()));
	}
	
	
	@PreAuthorize("hasRole('owner') or hasRole('driver')")
	@RequestMapping(value = UrlPath.GET_ID+"/{userId}", method = RequestMethod.GET)
	public ResponseEntity<UserDTO> getById(@PathVariable Long userId ) {
		return new ResponseEntity<UserDTO> (userService.getById(userId), HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasRole('owner')")
	@RequestMapping(value = UrlPath.SHOW_ALL, method = RequestMethod.GET)
	public ResponseEntity<?> getAllUsers(@RequestParam(required = false) String username,
	        @RequestParam(defaultValue = UrlPath.DEFAULT_PAGE_INDEX) int page,
	        @RequestParam(defaultValue = UrlPath.DEFAULT_PAGE_SIZE) int size) {
		try {
			List<UserDTO> userDtos = new ArrayList<>();
			userDtos = userService.getAllUsers(username, page, size);
			
			if(!userDtos.isEmpty())
				return new ResponseEntity<>(userDtos, HttpStatus.OK);
			else
				return new ResponseEntity<String>(ErrorMessage.NO_USERS, HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
		      return new ResponseEntity<String>(ErrorMessage.ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Delete user - only owner can do it
	@PreAuthorize("hasRole('owner')")
	@RequestMapping(value = UrlPath.DELETE+"/{userId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
		
		return new ResponseEntity<String>("Deleted", HttpStatus.OK);
	}

	@PreAuthorize("hasRole('owner')")
	@RequestMapping(value = UrlPath.UPDATE+"/{userId}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDto) {
		UserDTO userDtoRes = userService.updateUser(userId, userDto); 
		
		if(userDtoRes != null) {
			return new ResponseEntity<UserDTO>(userDtoRes, HttpStatus.OK);
		}else
			return new ResponseEntity<String>("Unable to update", HttpStatus.NOT_MODIFIED);
	}
}
 