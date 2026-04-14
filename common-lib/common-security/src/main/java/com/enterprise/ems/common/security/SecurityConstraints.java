package com.enterprise.ems.common.security;

public final class SecurityConstraints {
	private SecurityConstraints() {}
		  public static final String HEADER_STRING = "Authorization";
		  public static final String TOKEN_PREFIX="Bearer ";
		  public static final String SIGINING_KEY="ems.jpa.secure.key.2024.enterprisesystem";// has to be Move to config server later
	
}
