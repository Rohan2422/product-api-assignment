package com.zest.productapi.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {

	private Integer id;
	private String productName;
	private String createdBy;
	private LocalDateTime createdOn;

}
