package com.zest.productapi.controller;

import com.zest.productapi.dto.ProductRequest;
import com.zest.productapi.dto.ProductResponse;
import com.zest.productapi.entity.Item;
import com.zest.productapi.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

	@Autowired
	ProductService productService;

	@PostMapping
	public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
		return new ResponseEntity<>(productService.create(request), HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ProductResponse getById(@PathVariable Integer id) {
		return productService.getById(id);
	}

	@GetMapping
	public Page<ProductResponse> getAll(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		Pageable pageable = PageRequest.of(page, size);
		return productService.getAll(pageable);
	}

	@PutMapping("/{id}")
	public ProductResponse update(@PathVariable Integer id, @Valid @RequestBody ProductRequest request) {
		return productService.update(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		productService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/items")
	public List<Item> getItemsByProduct(@PathVariable Integer id) {
		return productService.getItemsByProductId(id);
	}
}