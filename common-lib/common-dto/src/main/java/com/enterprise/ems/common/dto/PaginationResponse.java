package com.enterprise.ems.common.dto;

import org.springframework.data.domain.Page;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class PaginationResponse {
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;
	
	public static PaginationResponse fromPage(Page<?> page) {
		return PaginationResponse.builder()
				.page(page.getNumber())
				.size(page.getSize())
				.totalElements(page.getTotalElements())
				.totalPages(page.getTotalPages())
				.build();
	}

}
