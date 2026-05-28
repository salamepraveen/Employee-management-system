package com.ems.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ems.auth.dto.AuthResponse;
import com.ems.auth.dto.LoginRequest;
import com.ems.auth.dto.RegisterRequest;
import com.ems.auth.service.AuthService;
import com.ems.common.dto.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
		private final AuthService authService;
		
		@Autowired
		public AuthController(AuthService authService) {
			this.authService=authService;
		}
		
		
		public ResponseEntity<ApiResponse<AuthResponse>> register(
					@Valid @RequestBody RegisterRequest request
				){
			
			AuthResponse response= authService.register(request);
			return new ResponseEntity<>(
					ApiResponse.success("Registration successful",response),HttpStatus.CREATED
					
					);
		}
		
		public ResponseEntity<ApiResponse<AuthResponse>> login(
				@Valid @RequestBody LoginRequest request
				){
			AuthResponse response=authService.authenticate(request);
			
			return ResponseEntity.ok(ApiResponse.success("Login successful",response));
		}
}
