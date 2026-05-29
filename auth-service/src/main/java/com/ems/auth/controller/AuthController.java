package com.ems.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.access.prepost.PreAuthorize;

import com.ems.auth.entity.User;
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
		
		
		@PostMapping("/register")
		public ResponseEntity<ApiResponse<AuthResponse>> register(
					@Valid @RequestBody RegisterRequest request
				){
			
			AuthResponse response= authService.register(request);
			return new ResponseEntity<>(
					ApiResponse.success("Registration successful",response),HttpStatus.CREATED
					
					);
		}
		
		@PostMapping("/login")
		public ResponseEntity<ApiResponse<AuthResponse>> login(
				@Valid @RequestBody LoginRequest request
				){
			AuthResponse response=authService.authenticate(request);
			
			return ResponseEntity.ok(ApiResponse.success("Login successful",response));
		}

		@PutMapping("/users/{userId}/role")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<ApiResponse<String>> updateRole(
				@PathVariable Long userId,
				@RequestParam User.Role role) {
			
			authService.updateRole(userId, role);
			return ResponseEntity.ok(ApiResponse.success("Role updated successfully"));
		}
}
