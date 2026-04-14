package com.enterprise.ems.common.exception;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.enterprise.ems.common.dto.ApiResponse;
import com.enterprise.ems.common.dto.ErrorDetail;
import com.enterprise.ems.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex){

        log.warn("Business exception occurred: Code={}, Message={}",
                ex.getErrorCode().getCode(),
                ex.getMessage());

        ApiResponse<Void> response = ApiResponse.error(
                ex.getErrorCode().getCode(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex){
    	log.warn("Validation failed for request: {}",ex.getMessage());
    	List<ErrorDetail> details=new ArrayList<>();
    	for(FieldError error:ex.getBindingResult().getFieldErrors()) {
    		details.add(new ErrorDetail(error.getField(),error.getDefaultMessage()));
    	}
    	
    	ApiResponse<Void> response=ApiResponse.error(CommonErrorCode.INVALID_PARAMETER.getCode(),"Validation failed");
    	 // Note: You can add a 'details' field to ApiResponse if needed, 
        // but keeping it simple here to match our initial DTO design.
    	
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    	
    }
    
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception ex){
    	log.error("Unexpected error occured: ",ex);
    	ApiResponse<Void> response=ApiResponse.error(CommonErrorCode.INTERNAL_SERVER_ERROR.getCode(),CommonErrorCode.INTERNAL_SERVER_ERROR.getMessage()	);
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    	
    }
}
