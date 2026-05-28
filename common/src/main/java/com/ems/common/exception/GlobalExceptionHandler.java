package com.ems.common.exception;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ems.common.dto.ApiResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
		
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex){
		ApiResponse<Void> response=ApiResponse.error(
				HttpStatus.NOT_FOUND.value(),ex.getMessage()
				
				);
		return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse<Void>> handleBuisnessException(BusinessException ex){
		ApiResponse<Void> response=ApiResponse.error(ex.getStatusCode(), ex.getMessage());
		return new ResponseEntity<>(response,HttpStatus.valueOf(ex.getStatusCode()));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Map<String,String>>> handleValidationErrors(MethodArgumentNotValidException ex){
		Map<String,String> errors=new HashMap<>();
		for(FieldError error:ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(),error.getDefaultMessage());
		}
		ApiResponse<Map<String,String>> response=ApiResponse.<Map<String,String>>builder()
												.timestamp(LocalDateTime.now())
												.status(HttpStatus.BAD_REQUEST.value())
												.message("Validation failed")
												.data(errors)
												.build();
	
	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<Map<String,String>>> handleConstraintViolation(ConstraintViolationException ex){
		Map<String,String> errors=new HashMap<>();
		
		for(ConstraintViolation<?> violation: ex.getConstraintViolations()) {
			errors.put(violation.getPropertyPath().toString(), violation.getMessage());
		}
		
		ApiResponse<Map<String,String>> response=ApiResponse.<Map<String,String>>builder()
												.timestamp(LocalDateTime.now())
												.status(HttpStatus.BAD_REQUEST.value())
												.message("Constraint Voilation")
												.data(errors)
												.build();
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex){
		ApiResponse<Void> response=ApiResponse.error(
					HttpStatus.INTERNAL_SERVER_ERROR.value(),"AN Unexpected error occured: "+ex.getMessage()
				);
		return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
