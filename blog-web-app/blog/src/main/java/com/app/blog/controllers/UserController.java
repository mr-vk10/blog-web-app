package com.app.blog.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.blog.dto.LoginDto;
import com.app.blog.dto.RegisterUserDTO;
import com.app.blog.models.Users;
import com.app.blog.repository.UserRepository;
import com.app.blog.util.EntitiyHawk;
import com.app.blog.util.JWTUtils;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.app.blog.service.UserAuthService;

import org.springframework.http.HttpStatus;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 1460344
 */
@RestController
@RequestMapping("/")
public class UserController extends EntitiyHawk {
	
	@Autowired
	private JWTUtils jwtUtils;
	
	@Autowired
	private UserAuthService userAuthService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/login/")
    public ResponseEntity login(@RequestBody LoginDto loginDto) throws Exception {
        
    	System.out.println(loginDto);
    	
    	// Optional<Users> userOpt = userRepository.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
    	
    	Optional<Users> userOpt = userRepository.findByEmail(loginDto.getEmail());
    	
    	Users user = null;
    	
    	if(userOpt.isPresent()) {
    		user = userOpt.get();
    	}else {
    		return genericError("Invalid Username or Password");
    		// return genericError();
    	}
    	
    	try {
    		// authenticates user
    		authenticationManager
    			.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), loginDto.getPassword()));
    		
    	}catch(UsernameNotFoundException e) {
    		e.printStackTrace();
    		throw new Exception("User Not Found");
    	}catch(BadCredentialsException e) {
    		e.printStackTrace();
    		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    	}
    	
    	// Generate token if user credentials are correct
    	// Users user = userAuthService.loadUserByUsername(jwtRequest.getUsername());
    	
    	
    	
    	// String token = jwtUtil.generateToken(user);
    	String token = jwtUtils.generateToken(user.getUsername());
    	
    	System.out.println("JWT Token: "+token);
    	
    	// return ResponseEntity.ok(new JwtResponse(token));
    	// return ResponseEntity.ok(token);
    	return genericSuccess(token);
    }
	
	@PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterUserDTO registerUserDTO) throws Exception {
        
    	System.out.println(registerUserDTO);
    	
    	Users user = new Users();
    	
    	try {
    		
    		if(registerUserDTO.getEmail()==null || registerUserDTO.getEmail().equalsIgnoreCase("")) {
    			
    			return genericError("email Email cannot be blank");
    			// return ResponseEntity.ok("email Email cannot be blank");
    		}
    		
    		user.setEmail(registerUserDTO.getEmail());
    		user.setPassword(registerUserDTO.getPassword());
    		user.setUserName(registerUserDTO.getName());
    		
    		userRepository.save(user);
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    		throw new Exception(e);
    	}
    	
    	
    	return genericSuccess("User Registered");
    }

}
