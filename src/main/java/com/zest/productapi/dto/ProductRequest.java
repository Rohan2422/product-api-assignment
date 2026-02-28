package com.zest.productapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductRequest {

	@NotBlank(message = "Product name is required")
	private String productName;

	@NotBlank(message = "Created by is required")
	private String createdBy;

	private String modifiedBy;

}
