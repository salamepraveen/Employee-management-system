package com.ems.auth.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ems.auth.dto.AuthResponse;
import com.ems.auth.dto.LoginRequest;
import com.ems.auth.dto.RegisterRequest;
import com.ems.auth.entity.User;
import com.ems.auth.repository.UserRepository;
import com.ems.common.exception.BusinessException;

import jakarta.transaction.Transactional;

public class AuthService {
	
		private final UserRepository userRepository;
		private final PasswordEncoder passwordEncoder;
		private final AuthenticationManager authenticationManager;
		private final JwtService jwtService;
		
		@Autowired
		public AuthService(UserRepository userRepository,
							PasswordEncoder passwordEncoder,
							AuthenticationManager authenticationManager,
							JwtService jwtService) {
			this.userRepository=userRepository;
			this.passwordEncoder=passwordEncoder;
			this.authenticationManager=authenticationManager;
			this.jwtService=jwtService;
		}
		
		@Transactional
		public AuthResponse register(RegisterRequest request) {
			if(userRepository.existsByUsername(request.getUsername())) {
				throw new BusinessException("Username "+ request.getUsername()+" is already taken",409);
			}
			
			if (userRepository.existsByEmail(request.getEmail())) {
				throw new BusinessException("Email "+request.getEmail()+" is already registered",409);
			}
			
			String hashedPassword=passwordEncoder.encode(request.getPassword());
			
			User.Role role=request.getRole() != null ? request.getRole():User.Role.USER;
			
			User user= User.builder()
						.username(request.getUsername())
						.email(request.getEmail())
						.password(hashedPassword)
						.role(role)
						.build();
			user = userRepository.save(user);
			
			String token=jwtService.generateToken(user);
			
			return AuthResponse.builder()
					.accessToken(token)
					.tokenType("Bearer")
					.expiresIn(jwtService.getExpiration())
					.username(user.getUsername())
					.email(user.getEmail())
					.role(user.getRole().name())
					.issuedAt(LocalDateTime.now())
					.build();
			
		}
		
		@Transactional
		public AuthResponse authenticate(LoginRequest request) {
			try {
				Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
			}catch(AuthenticationException e) {
				throw new BusinessException("Invalid username or password",401);
			}
			
			User user=userRepository.findByUsername(request.getUsername()).get();
			
			user.setLastLogin(LocalDateTime.now());
			userRepository.save(user);
			
			String token=jwtService.generateToken(user);
			
			return AuthResponse.builder()
						.accessToken(token)
						.tokenType("Bearer")
						.expiresIn(jwtService.getExpiration())
						.username(user.getUsername())
						.email(user.getEmail())
						.role(user.getEmail())
						.issuedAt(LocalDateTime.now())
						.build();
			
		}

}
