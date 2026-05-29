package com.ems.auth.service;

import java.util.Base64;
import java.util.function.Function;
import java.util.*;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.ems.auth.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class JwtService {
		private final SecretKey secretKey;
		private final long jwtExpiration;
		
		public JwtService(@Value("${jwt.secret}") String secret,@Value("${jwt.expiration}") long expiration){
			this.jwtExpiration=expiration;
			this.secretKey=Keys.hmacShaKeyFor(Base64.getEncoder().encodeToString(secret.getBytes()).getBytes());
		}
		
		public String generateToken(User user) {
			Map<String, Object> claims = new HashMap<>();
			claims.put("role", user.getRole().name());
			claims.put("email", user.getEmail());
			claims.put("companyId", user.getCompanyId());
			
			return Jwts.builder()
					.claims(claims)
					.subject(user.getUsername())
					.issuedAt(new Date(System.currentTimeMillis()))
					.expiration(new Date(System.currentTimeMillis()+jwtExpiration))
					.signWith(secretKey)
					.compact();
		}
		
		public boolean isTokenvalid(String token, UserDetails userDetails) {
			final String Username = extractUsername(token);
			return (Username.equals(userDetails.getUsername())&& !isTokenExpired(token));
			
		}
		
		public boolean isTokenExpired(String token) {
			return extractExpiration(token).before(new Date());
		}
		
		public String extractUsername(String token) {
			return extractClaim(token, Claims::getSubject);
		}
		
		public String extractRole(String token) {
			return extractClaim(token, claims-> claims.get("role",String.class));
		}
		
		public Date extractExpiration(String token) {
			return extractClaim(token, Claims::getExpiration);
		}
		
		public <T> T extractClaim(String token,Function<Claims, T> claimsResolver) {
			final Claims claims=Jwts.parser()
					.verifyWith(secretKey)
					.build()
					.parseSignedClaims(token)
					.getPayload();
			
			return claimsResolver.apply(claims);
		}
		public long getExpiration() {
			return jwtExpiration;
		}
}
