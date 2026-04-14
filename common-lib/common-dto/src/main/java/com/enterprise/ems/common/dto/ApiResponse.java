package com.enterprise.ems.common.dto;

import java.time.LocalDateTime;



import lombok.*;


@Builder
@Data
public class ApiResponse<T> {
		
	private String status;
	private String code;
	private String message;
	private T data;
	private PaginationResponse pagination;
	private LocalDateTime timestamp;
	
	public static<T> ApiResponse<T> success(T data){
		return ApiResponse.<T>builder()
				.status("SUCCESS")
				.message("Operation completed successfully")
				.data(data)
				.timestamp(LocalDateTime.now())
				.build();
	}
	
	public static <T> ApiResponse<T> success(T data,PaginationResponse pagination){
		return ApiResponse.<T>builder()
				.status("SUCCESS")
				.message("Operation completed successfully")
				.data(data)
				.pagination(pagination)
				.timestamp(LocalDateTime.now())
				.build();
	}
	public static <T> ApiResponse<T> error(String errorCode,String message){
		return ApiResponse.<T>builder()
				.status("Error")
				.code(errorCode)
				.message(message)
				.timestamp(LocalDateTime.now())
				.build();
	}

	
}
