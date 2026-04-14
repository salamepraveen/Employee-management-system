package com.enterprise.ems.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
	
		INVALID_PARAMETER("COM_400","Invalid request parameter"),
		RESOURCE_NOT_FOUND("COM_404","Resource not found"),
		INTERNAL_SERVER_ERROR("COM_500","An unexpected internal server error occured"),
		UNAUTHORIZED_ACCESS("COM_401","Authentication is required"),
		ACCESS_DENIED("COM_403","You do not have permission to perform this action");
		
		private final String code;
	    private final String message;

}
