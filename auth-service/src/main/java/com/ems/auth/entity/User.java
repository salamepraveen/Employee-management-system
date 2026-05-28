package com.ems.auth.entity;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Role;

import com.ems.common.enitity.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name="users")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class User extends BaseEntity {
	
	
	@Column(nullable=false, unique=true,length=50)
	private String username;
	
	@Column(nullable=false, unique=true,length=100)
	private String email;
	
	@Column(nullable=false, unique=true, length=100)
	private String password;
	
	public enum Role{
		ADMIN,MANAGER,USER
	}
	@Enumerated(EnumType.STRING)
	@Column(nullable=false, length=20)
	@Builder.Default
	private Role role=Role.USER;
	
	@Column(name="is+account_non_locked")
	@Builder.Default
	private boolean isAccounntNonLocked=true;
	
	@Column(name="last_login")
	private LocalDateTime lastLogin;
}
