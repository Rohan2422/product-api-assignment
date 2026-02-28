package com.zest.productapi.service;

import com.zest.productapi.dto.ProductRequest;
import com.zest.productapi.dto.ProductResponse;
import com.zest.productapi.entity.Item;
import com.zest.productapi.entity.Product;
import com.zest.productapi.repository.ItemRepository;
import com.zest.productapi.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Override
	public ProductResponse create(ProductRequest request) {

		Product product = Product.builder().productName(request.getProductName()).createdBy(request.getCreatedBy())
				.createdOn(LocalDateTime.now()).build();

		Product saved = productRepository.save(product);

		return ProductResponse.builder().id(saved.getId()).productName(saved.getProductName())
				.createdBy(saved.getCreatedBy()).createdOn(saved.getCreatedOn()).createdBy(saved.getCreatedBy())
				.createdOn(saved.getCreatedOn()).build();
	}

	@Override
	public ProductResponse getById(Integer id) {

		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

		return ProductResponse.builder().id(product.getId()).productName(product.getProductName())
				.createdBy(product.getCreatedBy()).createdOn(product.getCreatedOn()).build();
	}

	@Override
	public Page<ProductResponse> getAll(Pageable pageable) {

		return productRepository.findAll(pageable)
				.map(product -> ProductResponse.builder().id(product.getId()).productName(product.getProductName())
						.createdBy(product.getCreatedBy()).createdOn(product.getCreatedOn()).build());
	}

	@Override
	public ProductResponse update(Integer id, ProductRequest request) {
		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
		product.setProductName(request.getProductName());
		product.setModifiedBy(request.getCreatedBy());
		product.setModifiedOn(LocalDateTime.now());
		Product updated = productRepository.save(product);
		return ProductResponse.builder().id(updated.getId()).productName(updated.getProductName())
				.createdBy(updated.getCreatedBy()).createdOn(updated.getCreatedOn()).build();
	}

	@Override
	public void delete(Integer id) {

		if (!productRepository.existsById(id)) {
			throw new RuntimeException("Product not found");
		}

		productRepository.deleteById(id);
	}

	@Override
	public List<Item> getItemsByProductId(Integer productId) {

		if (!productRepository.existsById(productId)) {
			throw new RuntimeException("Product not found");
		}

		return itemRepository.findByProductId(productId);

	}
}