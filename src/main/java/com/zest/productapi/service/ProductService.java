package com.zest.productapi.service;


import com.zest.productapi.dto.ProductRequest;
import com.zest.productapi.dto.ProductResponse;
import com.zest.productapi.entity.Item;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

	ProductResponse create(ProductRequest request);

	ProductResponse getById(Integer id);

	Page<ProductResponse> getAll(Pageable pageable);

	ProductResponse update(Integer id, ProductRequest request);

	void delete(Integer id);

	List<Item> getItemsByProductId(Integer productId);

}
