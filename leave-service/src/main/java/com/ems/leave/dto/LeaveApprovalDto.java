package com.ems.leave.dto;



import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveApprovalDto {
			
	
	@NotNull(message="Action is required (APPROVE or REJECT)")
	private Action action;
	
	@Size(max=500, message="Comment must not exceed 500 characters")
	private String managerComment;
	
	public enum Action{
		APPROVE,REJECT
	}
}
