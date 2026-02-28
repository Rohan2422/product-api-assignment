package com.zest.productapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import com.zest.productapi.dto.ProductRequest;
import com.zest.productapi.dto.ProductResponse;
import com.zest.productapi.entity.Item;
import com.zest.productapi.entity.Product;
import com.zest.productapi.repository.ItemRepository;
import com.zest.productapi.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private ItemRepository itemRepository;

	@InjectMocks
	private ProductServiceImpl productService;

	private Product product;

	@BeforeEach
	void setup() {
		product = Product.builder().id(1).productName("Laptop").createdBy("Admin").createdOn(LocalDateTime.now())
				.build();
	}

	// ================= CREATE =================

	@Test
	void create_ShouldReturnProductResponse() {

		ProductRequest request = new ProductRequest();
		request.setProductName("Laptop");
		request.setCreatedBy("Admin");

		when(productRepository.save(any(Product.class))).thenReturn(product);

		ProductResponse response = productService.create(request);

		assertNotNull(response);
		assertEquals("Laptop", response.getProductName());
		verify(productRepository).save(any(Product.class));
	}

	// ================= GET BY ID =================

	@Test
	void getById_WhenProductExists_ShouldReturnResponse() {

		when(productRepository.findById(1)).thenReturn(Optional.of(product));

		ProductResponse response = productService.getById(1);

		assertEquals("Laptop", response.getProductName());
	}

	@Test
	void getById_WhenProductNotExists_ShouldThrowException() {

		when(productRepository.findById(1)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> productService.getById(1));
	}

	// ================= GET ALL =================

	@Test
	void getAll_ShouldReturnMappedPage() {

		Pageable pageable = PageRequest.of(0, 10);
		Page<Product> page = new PageImpl<>(List.of(product));

		when(productRepository.findAll(pageable)).thenReturn(page);

		Page<ProductResponse> result = productService.getAll(pageable);

		assertEquals(1, result.getTotalElements());
		assertEquals("Laptop", result.getContent().get(0).getProductName());
	}

	// ================= UPDATE =================

	@Test
	void update_WhenProductExists_ShouldReturnUpdatedResponse() {

		ProductRequest request = new ProductRequest();
		request.setProductName("Updated");
		request.setCreatedBy("User1");

		when(productRepository.findById(1)).thenReturn(Optional.of(product));

		Product updatedProduct = Product.builder().id(1).productName("Updated").createdBy("Admin")
				.createdOn(product.getCreatedOn()).build();

		when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

		ProductResponse response = productService.update(1, request);

		assertEquals("Updated", response.getProductName());
		verify(productRepository).save(any(Product.class));
	}

	@Test
	void update_WhenProductNotExists_ShouldThrowException() {

		when(productRepository.findById(1)).thenReturn(Optional.empty());

		ProductRequest request = new ProductRequest();

		assertThrows(RuntimeException.class, () -> productService.update(1, request));
	}

	// ================= DELETE =================

	@Test
	void delete_WhenProductExists_ShouldDelete() {

		when(productRepository.existsById(1)).thenReturn(true);

		productService.delete(1);

		verify(productRepository).deleteById(1);
	}

	@Test
	void delete_WhenProductNotExists_ShouldThrowException() {

		when(productRepository.existsById(1)).thenReturn(false);

		assertThrows(RuntimeException.class, () -> productService.delete(1));
	}

	// ================= GET ITEMS =================

	@Test
	void getItemsByProductId_WhenProductExists_ShouldReturnItems() {

		when(productRepository.existsById(1)).thenReturn(true);

		when(itemRepository.findByProductId(1)).thenReturn(Collections.emptyList());

		List<Item> items = productService.getItemsByProductId(1);

		assertNotNull(items);
		verify(itemRepository).findByProductId(1);
	}

	@Test
	void getItemsByProductId_WhenProductNotExists_ShouldThrowException() {

		when(productRepository.existsById(1)).thenReturn(false);

		assertThrows(RuntimeException.class, () -> productService.getItemsByProductId(1));
	}
}