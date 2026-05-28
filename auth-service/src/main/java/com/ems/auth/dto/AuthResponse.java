package com.ems.auth.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
		
	private String accessToken;
	private String tokenType;
	private Long expiresIn;
	private String username;
	private String email;
	private String role;
	private LocalDateTime issuedAt;
	
}
